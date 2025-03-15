package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.sql.SQLException;

import model.User;

public final class UserDAO {
    public static final class UserManager {
        public static synchronized void createUser(User user) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.persist(user);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static void createUser

        private static final String DELETE_USER = "UPDATE tblUser SET status = 0 WHERE id = ?1";

        public static synchronized void deleteUser(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                em.createNativeQuery(DELETE_USER).setParameter(1, id).executeUpdate();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static void deleteUser

        /**
         *
         * Careful, retrieve user's information from the database first
         */
        public static synchronized void updateUser(User user) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    User dbUser = em.find(User.class, user.getId());

                    dbUser.setEmail(user.getEmail());
                    dbUser.setUsername(user.getUsername());
                    dbUser.setPhoneNumber(user.getPhoneNumber());
                    dbUser.setPassword(user.getPassword());
                    dbUser.setPersistentCookie(user.getPersistentCookie());
                    dbUser.setIsAdmin(user.getIsAdmin());
                    dbUser.setGoogleId(user.getGoogleId());
                    dbUser.setFacebookId(user.getFacebookId());
                    dbUser.setCredit(user.getCredit());
                    dbUser.setDisplayName(user.getDisplayName());
                    dbUser.setProfileStringResourceId(user.getProfileStringResourceId());
                    dbUser.setBio(user.getBio());
                    dbUser.setStatus(user.isStatus());

                    et.commit();

                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }

        }

        /**
         *
         * Careful, retrieve user's information from the database first
         */
        public static synchronized void updateCookie(int userId, String cookie)
                throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    User dbUser = em.find(User.class, userId);

                    dbUser.setPersistentCookie(cookie);

                    et.commit();

                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        }

    } // public static final class UserManager

    /**
     * 
     */
    public static final class UserFetcher {
        public static synchronized model.User getUser(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("User.findById", model.User.class).setParameter("id", id).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static model.User getUser

        public static synchronized model.User getUserFromUsername(String username)
                throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("User.findByUsername", model.User.class).setParameter("username", username)
                        .getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static model.User getUser

        public static synchronized model.User getUser(String cookie) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("User.findByPersistentCookie", model.User.class)
                        .setParameter("persistentCookie", cookie).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static model.User getUser

        private static final String GET_USER_BY_USER = "SELECT * FROM tblUser WHERE username = ?1 AND password = ?2";
        private static final String GET_USER_BY_EMAIL = "SELECT * FROM tblUser WHERE email = ?1 AND password = ?2";

        public static synchronized model.User getUser(String userOrEmail, String password)
                throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return (User) em.createNativeQuery(GET_USER_BY_USER, User.class).setParameter(1, userOrEmail)
                        .setParameter(2, password).getSingleResult();
            } catch (Exception e) {
            }

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return (User) em.createNativeQuery(GET_USER_BY_EMAIL, User.class).setParameter(1, userOrEmail)
                        .setParameter(2, password).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized model.User getUser

        private static final String GET_USER_WITH_GOOGLE = "SELECT * FROM tblUser WHERE googleId = ?1 AND email = ?2";

        public static synchronized model.User getUserFromGoogle(String id, String email) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return (User) em.createNativeQuery(GET_USER_WITH_GOOGLE, User.class).setParameter(1, id)
                        .setParameter(2, email).getSingleResult();
            } catch (NoResultException e) {
                return null;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized java.util.List<model.User> getUsers()
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("User.findAll", model.User.class).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<model.User> getUsers
    } // public static final class UserFetcher
}
