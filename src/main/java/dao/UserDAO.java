package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public final class UserDAO {
    private static final String LOCK_TABLE_USER_BY_ID = "SELECT * FROM tblUser WHERE id = ? FOR UPDATE";
    private static final String LOCK_TABLE_USER = "SELECT * FROM tblUser FOR UPDATE";

    public static final class UserManager {
        private static final String CREATE_USER = "INSERT INTO tblUser (id, email, username, phoneNumber, password, persistentCookie, googleID, facebookID, isAdmin, credit, displayName, profileStringResourceID, bio)\n" + //
        "VALUES (\n" + //
        "    (SELECT MIN(t1.id) + 1 FROM tblUser t1 LEFT JOIN tblUser t2 \n" + //
        "     ON t1.id + 1 = t2.id WHERE t2.id IS NULL),\n" + //
        "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?\n" + //
        ");";

        public static synchronized void createUser(Connection connection, User user) throws SQLException {
            try (Statement lockStatement = connection.createStatement();
                ResultSet lockResultSet = lockStatement.executeQuery(LOCK_TABLE_USER)) {
                
                    try (PreparedStatement addPS = connection.prepareStatement(CREATE_USER)) {
                        addPS.setString(1, user.getEmail());
                        addPS.setString(2, user.getUsername());
                        addPS.setString(3, user.getPhoneNumber());
                        addPS.setString(4, user.getPassword());
                        addPS.setString(5, user.getCookie());
                        addPS.setString(6, user.getLinkStatus().getGoogleId());
                        addPS.setString(7, user.getLinkStatus().getFacebookId());
                        addPS.setBoolean(8, false);
                        addPS.setLong(9, user.getCredit());
                        addPS.setString(10, user.getDisplayName());
                        addPS.setString(11, user.getProfilePicResource());
                        addPS.setString(12, user.getBio());

                        addPS.executeUpdate();

                        connection.commit();
                    }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } // public static void createUser

        private static final String DELETE_USER = "DELETE * FROM tblUser WHERE id = ?";

        public static synchronized void deleteUser(Connection connection, int id) throws SQLException {
            try (PreparedStatement lockPS = connection.prepareStatement(LOCK_TABLE_USER_BY_ID)) {
                lockPS.setInt(1, id);
                lockPS.executeQuery();    
            
                    try (PreparedStatement addPS = connection.prepareStatement(DELETE_USER)) {
                        addPS.setInt(1, id);

                        addPS.executeUpdate();

                        connection.commit();
                    }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } // public static void deleteUser

        private static final String UPDATE_USER = "UPDATE tblUser SET email = ?, username = ?, phoneNumber = ?, password = ?, persistentCookie = ?, googleID = ?, facebookID = ?, isAdmin = ?, credit = ?, displayName = ?, profileStringResourceID = ?, bio = ? WHERE id = ?";

        // Careful, retrieve user's information from the database first
        public static synchronized void updateUser(Connection connection, User user) throws SQLException{
            try (PreparedStatement lockPS = connection.prepareStatement(LOCK_TABLE_USER_BY_ID)) {
                lockPS.setInt(1, user.getId());
                lockPS.executeQuery();    
                
                try (PreparedStatement addPS = connection.prepareStatement(UPDATE_USER)) {
                    // 13
                    addPS.setString(1, user.getEmail());
                    addPS.setString(2, user.getUsername());
                    addPS.setString(3, user.getPhoneNumber());
                    addPS.setString(4, user.getPassword());
                    addPS.setString(5, user.getCookie());
                    addPS.setString(6, user.getLinkStatus().getGoogleId());
                    addPS.setString(7, user.getLinkStatus().getFacebookId());
                    addPS.setBoolean(8, false);
                    addPS.setLong(9, user.getCredit());
                    addPS.setString(10, user.getDisplayName());
                    addPS.setString(11, user.getProfilePicResource());
                    addPS.setString(12, user.getBio());

                    addPS.setInt(13, user.getId());
                    
                    addPS.executeUpdate();

                    connection.commit();
                    }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }

    } // public static final class UserManager
}
