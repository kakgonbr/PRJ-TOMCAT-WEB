package dao;

import org.hibernate.Hibernate;

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

                // product.getCategoryId();
                // service.Logging.logger.info(product.getProductImageList());
                // product.getShopId();
                // product.getProductItemList().forEach(model.ProductItem::getProductCustomizationList);
                // product.getProductItemList().stream().map(model.ProductItem::getProductCustomizationList).forEach(service.Logging.logger::info);
                // Hibernate.initialize(product.getCategoryId());
                // Hibernate.initialize(product.getAvailablePromotionId());
                Hibernate.initialize(product.getProductImageList());
                // Hibernate.initialize(product.getShopId());
                Hibernate.initialize(product.getProductItemList());
                product.getProductItemList().stream().map(model.ProductItem::getProductCustomizationList).forEach(Hibernate::initialize);

                return product;
 
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized model.Product getProductDetails

        public static synchronized model.ProductItem getProductItem(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("ProductItem.findById", model.ProductItem.class).setParameter("id", id)
                        .getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        private static final String GET_PRODUCTS_FROM_SHOP = "SELECT * FROM tblProduct WHERE shopId = ?1";

        public static synchronized java.util.List<model.Product> getShopProducts(int shopId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(GET_PRODUCTS_FROM_SHOP, model.Product.class)
                        .setParameter(1, shopId).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        private static final String GET_PRODUCTS_FROM_SHOP_BY_CATEGORY = "WITH category AS (SELECT id FROM tblCategory WHERE id = ?1 UNION ALL SELECT c.id FROM tblCategory c JOIN category ch ON c.parent_id = ch.id) SELECT TOP 10 * FROM tblProduct WHERE tblProduct.shopId = ?2 AND tblProduct.categoryId IN (SELECT id FROM category)";
        
        public static synchronized java.util.List<model.Product> getShopProductsByCategory(int shopId, int category) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(GET_PRODUCTS_FROM_SHOP_BY_CATEGORY, model.Product.class)
                        .setParameter(1, category).setParameter(2, shopId).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        private static final String GET_PRODUCTS_BY_CATEGORY = "WITH category AS (SELECT id FROM tblCategory WHERE id = ?1 UNION ALL SELECT c.id FROM tblCategory c JOIN category ch ON c.parent_id = ch.id) SELECT TOP 10 * FROM tblProduct WHERE tblProduct.categoryId IN (SELECT id FROM category)";

        public static synchronized java.util.List<model.Product> getCategoryProducts(int category) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNativeQuery(GET_PRODUCTS_BY_CATEGORY, model.Product.class)
                        .setParameter(1, category).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

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
        public static synchronized java.util.List<model.Product> getRecommendation(String query, int page, int category)
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createStoredProcedureQuery(GET_RECOMMENDATION, model.Product.class)
                        .registerStoredProcedureParameter("query", String.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("page", Integer.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("category", Integer.class, ParameterMode.IN)
                        .setParameter("query", query).setParameter("page", page).setParameter("category", category)
                        .getResultList();

            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<Product> getRecommendation

        public static synchronized java.util.List<model.Product> getRecommendation(String query, int page) throws java.sql.SQLException {
            return getRecommendation(query, page, 0);
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

        private static String MARK_PRODUCT_DELETE = "UPDATE tblProduct SET status = 0 WHERE id = ?1";

        public static synchronized void deleteProduct(int productId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.createNativeQuery(MARK_PRODUCT_DELETE).setParameter(0, productId);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void deleteProduct

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
