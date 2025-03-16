/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 *
 * @author hoahtm
 */
public class PromotionDAO {

    public static class PromotionFetcher {
        private static final String CHECK_PROMOTION_USAGE
                = "SELECT CASE WHEN EXISTS ("
                + "SELECT 1 FROM tblOrder "
                + "WHERE userId = ?1 AND promotionId = ?2"
                + ") THEN 1 ELSE 0 END";

        public static synchronized boolean checkPromotionUsage(int userID, int promotionID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return (boolean) em.createNativeQuery(CHECK_PROMOTION_USAGE)
                        .setParameter(1, userID)
                        .setParameter(2, promotionID)
                        .getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } //  public static synchronized checkPromotionUsage

        private static final String CHECK_AVAILABLE_PROMOTIONS
                = "SELECT * FROM tblPromotion WHERE expireDate >= GETDATE() "
                + "AND (ofAdmin = 1 OR creatorId = ?1)";

        public static synchronized java.util.List<model.Promotion> checkAvailablePromotions(int userID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(CHECK_AVAILABLE_PROMOTIONS, model.Promotion.class)
                        .setParameter(1, userID)
                        .getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    } //  public static synchronized java.util.List<model.Promotion> checkAvailablePromotions

    public static class PromotionManager {

        public static synchronized boolean addPromotion(int actorID, model.Promotion promotion) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();

                    em.persist(promotion);

                    et.commit();
                    return true;
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
