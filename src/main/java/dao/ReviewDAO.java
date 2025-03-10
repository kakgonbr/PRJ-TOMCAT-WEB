package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Review;


import java.util.List;

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
        /*wrong query -> lack productID -> pagination wrong */
        public static final String GET_REVIEW_PAGE_NEXT = "select *\r\n" + //
                        "from tblReview\r\n" + //
                        "where id > ?1\r\n" + //
                        "order by id\r\n" + //
                        "offset (?2 - ?3 - 1)*5 rows\r\n" + //
                        "fetch next 5 rows only";
        public static final String GET_REVIEW_PAGE_PREVIOUS = "select *\r\n" + //
                        "from tblReview\r\n" + //
                        "where id < ?1\r\n" + //
                        "order by id \r\n" + //
                        "offset (?2 - 1)*5 rows\r\n" + //
                        "fetch next 5 rows only";
        public static final String GET_OVERALL_RATING = "SELECT AVG(rate) AS AvarageRate FROM tblReview WHERE productId = ?1";

        public static synchronized List<Review> getReviewNext(int lastID, int curPage, int nextPage) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_REVIEW_PAGE_NEXT, Review.class).setParameter(1, lastID).setParameter(2, nextPage).setParameter(3, curPage);
                return query.getResultList();
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }

        /*basic get prev */
        public static synchronized List<Review> getReviewPrev(int lastID, int prevPage) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_REVIEW_PAGE_PREVIOUS, Review.class).setParameter(1, lastID).setParameter(2, prevPage);
                return query.getResultList();
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }

        public static synchronized Double getOverallRating(int productID) throws java.sql.SQLException  {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_OVERALL_RATING).setParameter(1, productID);
                Object result = query.getSingleResult();
                return result != null ? (Double)result : 0.0;
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }

        
    }
        
}

