package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
public class ChatDAO {
    public static class BoxManager {
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

        private static final String GET_BOXES_BY_USER = "SELECT * FROM tblChatBox WHERE (user1 = ?1 OR user2 = ?2)";

        public static synchronized java.util.List<model.ChatBox> getBoxes(int userId) throws java.sql.SQLException {

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                
                Query query = em.createNativeQuery(GET_BOXES_BY_USER, model.ChatBox.class).setParameter(1, userId).setParameter(2, userId);

                return query.getResultList();                
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static java.util.List<model.ChatBox> getBoxes

        private static final String GET_BOX_BY_USERS = "SELECT * FROM tblChatBox WHERE (user1 = ?1 AND user2 = ?2) OR (user1 = ?3 AND user2 = ?4)";

        public static synchronized model.ChatBox getBox(int user1, int user2)
                throws java.sql.SQLException {

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return (model.ChatBox) em.createNativeQuery(GET_BOX_BY_USERS, model.ChatBox.class).setParameter(1, user1).setParameter(2, user2).setParameter(3, user2).setParameter(4, user1).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException("CHATBOX DOES NOT EXIST");
            }
        } // public static model.ChatBox getBox
    } // public static class BoxManager

    public static class ContentManager {

        public static synchronized void createContent(int box, int sender, String message)
                throws java.sql.SQLException {

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    model.ChatContent content = new model.ChatContent();
                    content.setMessage(message);
                    content.setSenderId(em.getReference(model.User.class, sender));
                    content.setChatBoxId(em.getReference(model.ChatBox.class, box));

                    em.persist(content);
                    
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static void createContent

        private static final String GET_CONTENT_BY_BOX = "SELECT * FROM tblChatBoxContent WHERE chatBoxID = ?1";

        public static synchronized java.util.List<model.ChatContent> getContent(int box) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(GET_CONTENT_BY_BOX, model.ChatContent.class).setParameter(1, box).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static java.util.List<model.ChatContent> getContent
    }
}
