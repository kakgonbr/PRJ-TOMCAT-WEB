package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class NotificationDAO {
    public static class NotificationFetcher {
        private static final String FIND_FOR_ID = "SELECT * FROM tblNotification WHERE userId = ?1 OR userId IS NULL";

        public static synchronized java.util.List<model.Notification> fetchFor(int userId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(FIND_FOR_ID, model.Notification.class).setParameter(1, userId).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } //  public static synchronized java.util.List<model.Notification> fetchFor
    }

    public static class NotificationManager {
        /**
         * Used for both system-wide notifications (user is null) and user specific notifications
         * @param notification
         * @throws java.sql.SQLException
         */
        public static synchronized void add(model.Notification notification) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.persist(notification);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }

                // em.persist(em); // ?????????????? what was I on?
            }
        } // public static synchronized void add

        public static synchronized void markAsRead(model.Notification notification) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    model.Notification dbNotification = em.find(model.Notification.class, notification.getId());

                    dbNotification.setIsRead(true);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void markAsRead
    }
}
