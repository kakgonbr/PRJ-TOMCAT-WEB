package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChatDAO {
    public static class BoxManager {
        private static final String CREATE_BOX = "INSERT INTO tblChatBox (id, user1, user2)\n" + //
                "VALUES (\n" + //
                "    (SELECT MIN(t1.id) + 1 FROM tblChatBox t1 LEFT JOIN tblChatBox t2 \n" + //
                "     ON t1.id + 1 = t2.id WHERE t2.id IS NULL),\n" + //
                "    ?, ?\n" + //
                ");";

        public static synchronized void createBox(Connection connection, int user1, int user2) throws SQLException {

            try (PreparedStatement addPS = connection.prepareStatement(CREATE_BOX)) {
                addPS.setInt(1, user1);
                addPS.setInt(2, user2);

                addPS.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } // public static void createBox

        private static final String GET_BOXES_BY_USER = "SELECT * FROM tblChatBox WHERE (user1 = ?) OR (user2 = ?)";

        public static synchronized java.util.List<model.ChatBox> getBoxes(Connection connection, int userId)
                throws SQLException {

            try (PreparedStatement ps = connection.prepareStatement(GET_BOXES_BY_USER)) {
                ps.setInt(1, userId);

                ResultSet rs = ps.executeQuery();

                java.util.List<model.ChatBox> contents = new java.util.ArrayList<>();
                while (rs.next()) {
                    contents.add(model.ChatBox.fromResultSet(rs));
                }

                return contents;
            }
        } // public static java.util.List<model.ChatBox> getBoxes

        private static final String GET_BOX_BY_USERS = "SELECT * FROM tblChatBox WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)";

        public static synchronized model.ChatBox getBox(Connection connection, int user1, int user2)
                throws SQLException {

            try (PreparedStatement ps = connection.prepareStatement(GET_BOX_BY_USERS)) {
                ps.setInt(1, user1);
                ps.setInt(2, user2);
                ps.setInt(3, user2);
                ps.setInt(4, user1);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return model.ChatBox.fromResultSet(rs);
                }

                throw new java.sql.SQLException("CHATBOX DOES NOT EXIST");
            }

        } // public static model.ChatBox getBox
    } // public static class BoxManager

    public static class ContentManager {
        private static final String CREATE_CONTENT = "INSERT INTO tblChatBoxContent (id, chatBoxID, message, time, senderID)\n"
                + //
                "VALUES (\n" + //
                "    (SELECT MIN(t1.id) + 1 FROM tblChatBoxContent t1 LEFT JOIN tblChatBoxContent t2 \n" + //
                "     ON t1.id + 1 = t2.id WHERE t2.id IS NULL),\n" + //
                "    ?, ?, ?, ?\n" + //
                ");";

        public static synchronized void createContent(Connection connection, int box, int sender, String message)
                throws SQLException {

            try (PreparedStatement addPS = connection.prepareStatement(CREATE_CONTENT)) {
                addPS.setInt(1, box);
                addPS.setString(2, message);
                addPS.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                addPS.setInt(4, sender);

                addPS.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } // public static void createContent

        private static final String GET_CONTENT_BY_BOX = "SELECT * FROM tblChatBoxContent WHERE chatBoxID = ?";

        public static synchronized java.util.List<model.ChatContent> getContent(Connection connection, int box)
                throws SQLException {

            try (PreparedStatement ps = connection.prepareStatement(GET_CONTENT_BY_BOX)) {
                ps.setInt(1, box);

                ResultSet rs = ps.executeQuery();

                java.util.List<model.ChatContent> contents = new java.util.ArrayList<>();
                while (rs.next()) {
                    contents.add(model.ChatContent.fromResultSet(rs));
                }

                return contents;
            }

        } // public static java.util.List<model.ChatContent> getContent
    } // public static class ContentManager
}
