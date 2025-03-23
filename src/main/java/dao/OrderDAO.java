package dao;

import org.hibernate.Hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.CartItem;
import model.ProductOrder;

public class OrderDAO {
    public static class OrderManager {
        public static synchronized int createOrder(ProductOrder order) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    // JPA fetches and updates the order's ID here
                    em.persist(order);

                    et.commit();

                    return order.getId();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized int createOrder

        public static synchronized ProductOrder getOrder(int id, boolean fetch) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                ProductOrder order = em.createNamedQuery("ProductOrder.findById", ProductOrder.class).setParameter("id", id)
                .getSingleResult();

                if (fetch) {
                    Hibernate.initialize(order.getOrderedItemList());
                }
                
                return order;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized ProductOrder getOrder

        public static synchronized ProductOrder getOrder(int id) throws java.sql.SQLException {
            return getOrder(id, false);
        } // public static synchronized ProductOrder getOrder

        public static synchronized void markCompleted(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    // find is used instead of getReference because it fetches the entire record
                    // (not lazily fetched)
                    ProductOrder order = em.find(ProductOrder.class, id);

                    order.setStatus(true);

                    em.merge(order);

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void markCompleted

        /**
         * DO NOT EDIT. This method is for deleting orders that did not go through.<br></br>
         * The status column ONLY indicates that the order is still awaiting payment.<br></br>
         * For orders without payment, they are invalid and must be removed.
         * @param id
         * @throws java.sql.SQLException
         */
        public static synchronized void deleteOrder(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.remove(em.find(model.ProductOrder.class, id));

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized boolean isCompleted(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("ProductOrder.findById", ProductOrder.class).getSingleResult().isStatus();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized boolean isCompleted

        private static final String UPDATE_ORDER_PRICE = "WITH total AS ( SELECT SUM(totalPrice + shippingCost) AS total FROM tblOrderedItem WHERE orderId = ?1) UPDATE tblOrder SET finalPrice = (SELECT CASE WHEN p.id IS NULL THEN t.total WHEN p.type = 1 THEN t.total - p.value WHEN p.type = 0 THEN t.total * (1 - p.value / 100.0) ELSE t.total END FROM total t LEFT JOIN tblOrder o ON o.id = ?1 LEFT JOIN tblPromotion p ON p.id = o.promotionId ) WHERE id = ?1;";

      /**
         * Call this after every request the user made to add items from cart to order.
         * 
         * @param id
         * @throws java.sql.SQLException
         */
        public static synchronized void updatePrice(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    if (em.createNativeQuery(UPDATE_ORDER_PRICE).setParameter(1, id)
                            .executeUpdate() != 1)
                        throw new java.sql.SQLException("CANNOT UPDATE THE PRICE OF ORDER, THE UPDATE COUNT IS NOT 1");

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void updatePrice
    } // public static class OrderManager

    public static class OrderedItemManager {
        private static final String INSERT_INTO_ORDER = "INSERT INTO tblOrderedItem (orderId, productItemId, quantity, totalPrice, shippingCost) VALUES (?1, ?2, ?3, ?4, ?5)";
        private static final String DELETE_FROM_CART_ITEM = "DELETE FROM tblCartItem WHERE id = ?1";
        private static final String REMOVE_FROM_PRODUCT_ITEM = "UPDATE tblProductItem SET stock = ((SELECT stock FROM tblproductItem WHERE id = ?2) - ?1) WHERE id = ?2";

        /**
         * Try to verify that these items belong to the correct cart and the correct
         * user before calling.<br>
         * </br>
         * 
         * @param items
         * @return
         * @throws java.sql.SQLException
         */
        public static synchronized void transferFromCart(int orderId, java.util.List<model.CartItemWrapper> items)
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    for (final model.CartItemWrapper item : items) {
                        service.Logging.logger.info("Inserting into tblOrderedItem: orderId {}, productItemId {}, quantity {}, totalPrice {}, shippingCost {}", orderId, item.getProductItem().getId(), item.getQuantity(), item.getProductWrapper().getPromotion() == null
                        ? item.getProductItem().getPrice()
                        : (item.getProductWrapper().getPromotion().getType()
                                ? item.getProductItem().getPrice()
                                        - item.getProductWrapper().getPromotion().getValue()
                                : item.getProductItem().getPrice()
                                        * (100.0 - item.getProductWrapper().getPromotion().getValue())
                                        / 100.0), 0);


                        em.createNativeQuery(INSERT_INTO_ORDER).setParameter(1, orderId).setParameter(2, item.getProductItem().getId())
                                .setParameter(3, item.getQuantity())
                                .setParameter(4, item.getProductWrapper().getPromotion() == null
                                        ? item.getProductItem().getPrice()
                                        : (item.getProductWrapper().getPromotion().getType()
                                                ? item.getProductItem().getPrice()
                                                        - item.getProductWrapper().getPromotion().getValue()
                                                : item.getProductItem().getPrice()
                                                        * (100.0 - item.getProductWrapper().getPromotion().getValue())
                                                        / 100.0)).setParameter(5, 0) // TODO: ADD SHIPPING COST
                                .executeUpdate();

                        service.Logging.logger.info("Deleting from tblCartItem id {}", item.getId());
                        em.createNativeQuery(DELETE_FROM_CART_ITEM).setParameter(1, item.getId()).executeUpdate();
                        
                        service.Logging.logger.info("Updating product item id {}, new stock {}", item.getProductItem().getId(), item.getQuantity());
                        em.createNativeQuery(REMOVE_FROM_PRODUCT_ITEM).setParameter(1, item.getQuantity()).setParameter(2, item.getProductItem().getId()).executeUpdate();
                    }

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }

                    throw new java.sql.SQLException(e);
                }
            }
        } // public static synchronized void transferFromCart

    }
}
