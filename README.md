# Contribution
**Read the documentation below to understand how to commit, practices to follow, etc.**

---
# Web Application META and INF
## `web.xml`
Always use the `web.xml` file to map servlets, listeners, filters. Do not use annotation.

# SQL Database
## Tables
### Restrictions
These restrictions are imposed by the SQL tables, not ones that are later implemented in code. For example, the column `email` of `tblUser` accepts all Unicode String values as long as they are within 50 characters. Whether the email format is used should be checked on the web application, for easier implementation and management.

Format checks and other input validations should be done **ON THE WEB APPLICATION** to avoid extra communication overhead between the two servers.

**See DATABASE.sql for table information**

### Insertion
Insertion is done mostly through JPA now.
```sql
TO BE CHANGED
```
---
# Front-end
## Resources
Resources are placed inside `./resources`. The content of this directory will be moved to where the server looks up resources.

To look up resources, add a mapping in the database, as a record in the table `tblResourceMap`. The key is the identifier of the file, the the other column is the path string.

Example: Adding a new JavaScript file
- Add an .js file in `./resources`
- Add a mapping `test_js -> test.js` as a record in the database
- Trigger a build on Jenkins, either through a GitHub push, or manually.
- On the front-end, look up using the linK `${pageContext.request.contextPath}/resources/test_js`. Where `resources` is the context path of the servlet responsible for fetching, `test_js` is the identifier.

# Java
## Practices
Add documentation to methods if what they do are not obvious enough. Documentation can be added using:
```java
/**
 * create_instance
 * @param array of attributes for instance containing web, db, arrival_rate, response_time for instance 
 * respectively.
 * @return Instance object
 */
```

Consider adding `synchronized` for better concurrency.

Only import what is needed, full class path should be used for classes that only get used once or a few times.

## Runtime Environment Variables
Set at /opt/tomcat/bin/setenv.sh

## Servlets
If an object or value should be sent to a JSP file to be displayed or another servlet to process, put it inside the `request` object using `request.setAttribute();`. Only for in-class attribute passing, method arguments are allowed. Minimize using arguments as much as possible to avoid compromises.

For forwarding, use "WEB-INF/jsp/...", for forwarding to other servlets, use `request.getContextPath()` and concatenate with the servlet's path.

## JSP
**All JSP files MUST be placed in WEB-INF/jsp/** as they are meant to be forwarded to with attributes set.

See `template.jsp` to see the general template for JSP files.

When creating JSP files, put their file path inside `config.Config.JSPMapper`
WHen forwarding to JSP files, use mappings in `config.Config.JSPMapper`

## Database Access Objects
### Generating JPA Model classes
Use Netbeans' tools to generate these classes.

For relationships, decide the fetch type. For example, if the ChatBox object is expected to also have a User object in it, not just the ID, use:
```java
@ManyToOne(fetch = FetchType.EAGER)
```

Otherwise, use `FetchType.LAZY`, which will only makes the id visible.

### Creating DAO Classes
When creating DAO classes, no interface is needed. Instead use inner classes to separate and organize DAO methods. How to separate the inner classes is detailed in the diagram.

The methods also must throw the exception `java.sql.SQLException`. Any catching of exceptions must be thrown again as these methods are not meant to handle the exceptions by themselves.

**All DAO Classes must be static**

**Examples for DAO methods**:
Inserting:
```java
public static synchronized void createBox(int user1, int user2) throws java.sql.SQLException {
    try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();

            model.ChatBox box = new model.ChatBox();
            box.setUser1(em.getReference(model.User.class, user1));
            box.setUser2(em.getReference(model.User.class, user2));

            em.persist(box);

            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }

            throw new java.sql.SQLException(e);
        }
    }
} // public static synchronized void createBox
```
Querying:
```java
public static synchronized java.util.List<model.Product> getProducts() throws java.sql.SQLException {
    try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        return em.createNamedQuery("Product.findAll", model.Product.class).getResultList();
    } catch (Exception e) {
        throw new java.sql.SQLException(e);
    }
} // public static synchronized java.util.List<Product> getProducts
```
Named queries are generated by the tool, placed in the model class.

Updating:
```java
public static synchronized void markCompleted(int id) throws java.sql.SQLException {
    try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();

            // find is used instead of getReference because it fetches the entire record (not lazily fetched)
            ProductOrder order = em.find(ProductOrder.class, id);

            order.setStatus(true);

            em.merge(order);

            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }

            throw new java.sql.SQLException(e);
        }
    }
} // public static synchronized void markCompleted
```

Procedure Calling:
```java
private static final String GET_RECOMMENDATION = "GetRecommendation";

public static synchronized java.util.List<model.Product> getRecommendation(String query)
        throws java.sql.SQLException {
    try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        return em.createStoredProcedureQuery(GET_RECOMMENDATION).registerStoredProcedureParameter("query", String.class, ParameterMode.IN).setParameter("query", query).getResultList();
    } catch (Exception e) {
        throw new java.sql.SQLException(e);
    }
} // public static synchronized java.util.List<Product> getRecommendation
```

Native Query Execution:
```java
private static final String GET_BOX_BY_USERS = "SELECT * FROM tblChatBox WHERE (user1 = ?1 AND user2 = ?2) OR (user1 = ?3 AND user2 = ?4)";

public static synchronized model.ChatBox getBox(int user1, int user2)
        throws java.sql.SQLException {

    try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        return (model.ChatBox) em.createNativeQuery(GET_BOX_BY_USERS, model.ChatBox.class).setParameter(1, user1).setParameter(2, user2).setParameter(3, user2).setParameter(4, user1).getSingleResult();
    } catch (Exception e) {
        throw new java.sql.SQLException("CHATBOX DOES NOT EXIST");
    }
} // public static model.ChatBox getBox
```

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

For troubleshooting and other functionalities, visit the Jenkins page.

# Names
## Session objects
```
    "user" : model.User
```

## Cookies
Can be found at config.Config.CookieMapper

## JSP Files
Can be found at config.Config.JSPMapper

# JPA Specific Nuances
New ways to write add/update/remove queries.
Old native queries can be used, abeit the implementation is a little different.

Some classes need wrappers, as we shouldn't touch the classes generated by the tool.