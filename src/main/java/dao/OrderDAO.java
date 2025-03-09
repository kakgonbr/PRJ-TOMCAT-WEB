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
                return em.createNamedQuery("ProductOrder.findById", ProductOrder.class).getSingleResult();
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
                return em.createNamedQuery("ProductOrder.findById", ProductOrder.class).getSingleResult().getStatus();
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
        }
    } // public static class OrderManager

    public static class OrderedItemManager {
        /**
         * Try to verify that these items belong to the correct cart and the correct
         * user before calling.<br>
         * </br>
         * 
         * @param items
         * @return
         * @throws java.sql.SQLException
         */
        public static synchronized int transferFromCart(int orderId, java.util.List<CartItem> items)
                throws java.sql.SQLException {
            // TODO: IMPLEMENT THIS AFTER CART AND CART DAO
            throw new java.sql.SQLException("NOT IMPLEMENTED");
        } // public static synchronized void transferFromCart
    }
}
