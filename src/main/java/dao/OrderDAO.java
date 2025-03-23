package dao;

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

        public static synchronized ProductOrder getOrder(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("ProductOrder.findById", ProductOrder.class).setParameter("id", id).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
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

        public static synchronized boolean isCompleted(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("ProductOrder.findById", ProductOrder.class).getSingleResult().isStatus();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized boolean isCompleted

        private static final String UPDATE_ORDER_PRICE = "UPDATE tblOrder SET finalPrice = (SELECT SUM(totalPrice + shippingCost) FROM tblOrderedItem WHERE orderId = ?1) WHERE id = ?2";

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

                    if (em.createNativeQuery(UPDATE_ORDER_PRICE).setParameter(1, id).setParameter(2, id)
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

        /**
         * Try to verify that these items belong to the correct cart and the correct
         * user before calling.<br>
         * </br>
         * 
         * @param items
         * @return
         * @throws java.sql.SQLException
         */
        public static synchronized void transferFromCart(int orderId, java.util.List<CartItem> items,
                model.Promotion promotion) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    for (final model.CartItem item : items) {
                        em.createNativeQuery(INSERT_INTO_ORDER).setParameter(1, orderId).setParameter(2, item.getId())
                                .setParameter(3, item.getQuantity())
                                .setParameter(4,
                                        promotion == null ? item.getProductItemId().getPrice().longValue()
                                                : (promotion.getType()
                                                        ? item.getProductItemId().getPrice().longValue()
                                                                - promotion.getValue()
                                                        : item.getProductItemId().getPrice().longValue()
                                                                * (100.0 - promotion.getValue()) / 100.0)).setParameter(5, 0) // TODO: ADD SHIPPING COST
                                .executeUpdate();

                        em.createNativeQuery(DELETE_FROM_CART_ITEM).setParameter(1, item.getId()).executeUpdate();
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
