# Contribution
**Read the documentation below to understand how to commit, practices to follow, etc.**

---
# SQL Database
## Tables
### Restrictions
These restrictions are imposed by the SQL tables, not ones that are later implemented in code. For example, the column `email` of `tblUser` accepts all Unicode String values as long as they are within 50 characters. Whether the email format is used should be checked on the web application, for easier implementation and management.

Format checks and other input validations should be done **ON THE WEB APPLICATION** to avoid extra communication overhead between the two servers.

**See DATABASE.sql for table information**

### Insertion
The following format is used for insertions, sacrificing some performance for storage efficiency:
This is not thread-safe, refer below to see how to achieve thread-safety.
```sql
INSERT INTO tblTable (id, column1, column2)
VALUES (
    (SELECT MIN(t1.id) + 1 FROM tblTable t1 LEFT JOIN tblTable t2 
     ON t1.id + 1 = t2.id WHERE t2.id IS NULL),
    'value1', 'value2'
);
```
## Thread-Safety
`SELECT ... FOR UPDATE` is used on every update to ensure thread-safety.

Example usage:
```java
public void deleteProduct(int productId) {
    String selectQuery = "SELECT product_id FROM products WHERE product_id = ? FOR UPDATE";
    String deleteQuery = "DELETE FROM products WHERE product_id = ?";

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false); // Start transaction

        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalStateException("Product not found");
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, productId);
                deleteStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
        } catch (Exception e) {
            conn.rollback(); // Rollback if anything goes wrong
            throw e;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```
---
# Java
## Runtime Environment Variables
Set at /opt/tomcat/bin/setenv.sh

## Servlets
If an object or value should be sent to a JSP file to be displayed or another servlet to process, put it inside the `request` object using `request.setAttribute();`. Only for in-class attribute passing, method arguments are allowed. Minimize using arguments as much as possible to avoid compromises.

## Database Access Objects
### Acquiring the `java.sql.Connection` Object
```java
// From a static method
DatabaseConnection.getConnection()

// From the servlet context
(java.sql.Connection) getServletContext().getAttribute("DB_CONNECTION"), userId, discountCode)
```
**IMPORANT:**  Getting the `Connection` object from the servlet context **does not** check if the connection is valid and will not trigger a lockdown if the connection to the database is closed. Avoid it as much as possible.

### Creating DAO Classes
When creating DAO classes, no interface is needed. Instead use inner classes to separate and organize DAO methods. How to separate the inner classes is detailed in the diagram.

**All DAO Classes must be static**

### Thread-Safety
See [here](#thread-safety).

## Logging
Logger used: `log4j 2.4.3`
All critical operations require logging.

How to log:
```java
service.Logging.logger.info("Database connection established.");

service.Logging.logger.fatal("FAILED TO CONNECT TO THE DATABASE. REASON: '{}'", e.getMessage());
```

Reference: https://logging.apache.org/log4j/2.x/jakarta.html
# Build
Simply commit to the main branch if you wish the web application to be automatically built and deployed.
