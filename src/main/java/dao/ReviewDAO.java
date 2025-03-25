package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Review;


import java.util.List;

import org.hibernate.Hibernate;

public class ReviewDAO {
    
    public static class ReviewManager {
        public static synchronized boolean addReview(Review review) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.persist(review);
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

        public static synchronized boolean removeReview(int reviewID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    Review review = em.find(Review.class, reviewID);
                    review.setStatus(false);
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
    }

    public static class ReviewFetcher {
        
        public static final String GET_REVIEWS_BY_PRODUCT_PAGED = "SELECT r FROM Review r JOIN FETCH r.userId WHERE r.productId.id = ?1 AND r.status = true ORDER BY r.id DESC";
        public static final String COUNT_REVIEWS_BY_PRODUCT = "SELECT COUNT(r) FROM Review r WHERE r.productId.id = ?1 AND r.status = true";
        public static final String GET_OVERALL_RATING = "SELECT AVG(r.rate) FROM Review r WHERE r.productId.id = ?1 AND r.status = true";
    
        public static synchronized List<Review> getReviewsByProductId(int productId, int page, int pageSize) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                List<Review> reviews = em.createQuery(GET_REVIEWS_BY_PRODUCT_PAGED, Review.class).setParameter(1, productId)
                        .setFirstResult((page - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList();

                reviews.stream().map(Review::getUserId).map(model.User::getProfileStringResourceId).forEach(Hibernate::initialize);

                return reviews;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    
        public static synchronized long getTotalReviews(int productId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createQuery(COUNT_REVIEWS_BY_PRODUCT, Long.class).setParameter(1, productId).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized Double getOverallRating(int productID) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Double result = em.createQuery(GET_OVERALL_RATING, Double.class).setParameter(1, productID).getSingleResult();
                return result != null ? result : 0.0;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    }
        
}

