package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.ParameterMode;

public class ProductDAO {
    public static class ProductFetcher {
        public static synchronized java.util.List<model.Product> getProducts() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Product.findAll", model.Product.class).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<Product> getProducts

        public static synchronized model.Product getProduct(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Product.findById", model.Product.class).setParameter("id", id)
                        .getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized model.Product getProduct

        private static final String GET_RECOMMENDATION = "GetRecommendation";

        public static synchronized java.util.List<model.Product> getRecommendation(String query)
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createStoredProcedureQuery(GET_RECOMMENDATION).registerStoredProcedureParameter("query", String.class, ParameterMode.IN).setParameter(1, query).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<Product> getRecommendation
    } // public static class ProductFetcher

    /**
     * Try not to touch this, its methods are mostly run by the job handler
     */
    public static class TFIDF {
        private static final String COMPUTE_TFIDF = "ComputeTFIDF";

        public static synchronized void computeTFIDF() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.createStoredProcedureQuery(COMPUTE_TFIDF).execute();

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void computeTFIDF
    }

    public static class ProductManager {
        // TODO: Implement
    }
}
