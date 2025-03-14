package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

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

        /**
         * Eagerly fetches most information about a product, used for displaying the
         * product detail screen
         * 
         * @param id
         * @return
         * @throws java.sql.SQLException
         */
        public static synchronized model.Product getProductDetails(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                model.Product product = em.find(model.Product.class, id);

                product.getCategoryId();
                product.getProductImageList();
                product.getShopId();
                product.getProductItemList().forEach(model.ProductItem::getProductCustomizationList);

                return product;

            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized model.Product getProductDetails

        // TODO: PAGINATE THIS
        public static synchronized java.util.List<model.Review> getReviews(int productId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.find(model.Product.class, productId).getReviewList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<model.Review> getReviews

        private static final String GET_RECOMMENDATION = "GetRecommendation";

        @SuppressWarnings("unchecked")
        public static synchronized java.util.List<model.Product> getRecommendation(String query, int page, String category)
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createStoredProcedureQuery(GET_RECOMMENDATION, model.Product.class)
                        .registerStoredProcedureParameter("query", String.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("page", Integer.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("categoryName", String.class, ParameterMode.IN)
                        .setParameter("query", query).setParameter("page", page).setParameter("categoryName", category)
                        .getResultList();

            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<Product> getRecommendation

        public static synchronized java.util.List<model.Product> getRecommendation(String query, int page) throws java.sql.SQLException {
            return getRecommendation(query, page, "");
        }

        private static final String GET_CUSTOMIZATIONS = "SELECT tblProductCustomization.* FROM tblProductCustomization INNER JOIN tblProductItem ON productItemId = tblProductItem.id WHERE tblProductItem.productId = ?1";

        public static synchronized java.util.List<model.ProductCustomization> getCustomizations(int productId)
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(GET_CUSTOMIZATIONS, model.ProductCustomization.class)
                        .setParameter(1, productId).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<model.ProductCustomization>
          // getCustomizations
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
        public static synchronized void addProduct(model.Product product) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.persist(product);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }

        } // public static synchronized void addProduct

        /**
         * Make sure the product has its category set and the category exists in the
         * database
         * 
         * @param product
         * @throws java.sql.SQLException
         */
        public static synchronized void editProduct(model.Product product) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    model.Product dbProduct = em.find(model.Product.class, product.getId());

                    dbProduct.setAvailablePromotionId(product.getAvailablePromotionId());
                    dbProduct.setCategoryId(product.getCategoryId());
                    dbProduct.setDescription(product.getDescription());
                    dbProduct.setImageStringResourceId(product.getImageStringResourceId());
                    dbProduct.setName(product.getName());
                    dbProduct.setShopId(product.getShopId());
                    dbProduct.setStatus(product.isStatus());

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void editProduct

        public static synchronized void addCustomizations(int productId,
                java.util.List<model.ProductCustomization> customizations) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    for (final model.ProductCustomization customization : customizations) {
                        model.VariationValue variationValue = customization.getVariationValueId();

                        if (em.find(model.Variation.class, variationValue) == null)
                            continue;

                        // customization's VariationValue gets its ID assigned here.
                        em.persist(variationValue);

                        em.persist(customization);
                    }

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void addCustomizations
    }
}
