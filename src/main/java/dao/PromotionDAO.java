/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

/**
 *
 * @author hoahtm
 */
public class PromotionDAO {

    public static class PromotionFetcher {
        // private static final String GET_AVAILABLE_FOR_USER = "SELECT * FROM tblPromotion WHERE ofAdmin = 1 AND tblPromotion.id NOT IN (SELECT promotionId from tblPromotionUsage WHERE userId = ?1)";

        // public static synchronized java.util.List<model.Promotion> getAvaialblePromotionsFor(int userId) throws java.sql.SQLException {
        //     try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        //         return em.createNativeQuery(GET_AVAILABLE_FOR_USER, model.Promotion.class).setParameter(1, userId).getResultList();
        //     } catch (Exception e) {
        //         throw new java.sql.SQLException(e);
        //     }
        // } // public static synchronized java.util.List<model.Promotion> getAvaialblePromotionsFor

        private static final String CHECK_PROMOTION_USAGE = "SELECT * FROM tblOrder WHERE userId = ?1 AND promotionId = ?2";
        /**
         * Returns an object indicating that the promotion is used in an order by a user, null otherwise
         * @param userID
         * @param promotionID
         * @return
         * @throws java.sql.SQLException
         */
        public static synchronized Object checkPromotionUsage(int userID, int promotionID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(CHECK_PROMOTION_USAGE)
                                        .setParameter(1, userID)
                                        .setParameter(2, promotionID)
                                        .getSingleResult(); //?????????????????
            } catch (NoResultException e) {
                return null;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } //  public static synchronized checkPromotionUsage

        private static final String CHECK_AVAILABLE_PROMOTIONS
                = "SELECT * FROM tblPromotion WHERE expireDate >= GETDATE() "
                + "AND ofAdmin = 1 AND tblPromotion.id NOT IN (SELECT promotionID FROM tblOrder WHERE userId = ?1)";

        public static synchronized java.util.List<model.Promotion> checkAvailablePromotions(int userID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(CHECK_AVAILABLE_PROMOTIONS, model.Promotion.class)
                        .setParameter(1, userID)
                        .getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
        
        private static final String CHECK_AVAILABLE_PROMOTIONS_BY_CREATOR
                = "SELECT * FROM tblPromotion WHERE expireDate >= GETDATE() AND creatorId = ?1";

        public static synchronized java.util.List<model.Promotion> checkAvailablePromotionsByCreator(int userID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(CHECK_AVAILABLE_PROMOTIONS_BY_CREATOR, model.Promotion.class)
                        .setParameter(1, userID)
                        .getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized model.Promotion getPromotion(int promotionId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Promotion.findById", model.Promotion.class)
                        .setParameter("id", promotionId).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    } //  public static synchronized java.util.List<model.Promotion> checkAvailablePromotions

    public static class PromotionManager {

        public static synchronized model.Promotion addPromotion(model.Promotion promotion) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();

                    em.persist(promotion);

                    et.commit();
                    return promotion;
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }
        private static final String REMOVE_EXPIRED_PROMOTIONS
                = "UPDATE tblPromotion SET status = 0 WHERE id = ? AND expireDate <= GETDATE()";

        public static synchronized void removeExpiredPromotions() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.createNativeQuery(REMOVE_EXPIRED_PROMOTIONS).executeUpdate();
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }
    }
}
