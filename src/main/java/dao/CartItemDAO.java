package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.CartItem;

public class CartItemDAO {
    public static class CartItemManager {
        public static synchronized void createCartItem(CartItem cartItem) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    em.persist(cartItem);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized CartItem getCartItem(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.find(CartItem.class, id);
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized void updateCartItem(CartItem cartItem) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    em.merge(cartItem);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized void deleteCartItem(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    CartItem cartItem = em.find(CartItem.class, id);
                    if (cartItem != null) {
                        em.remove(cartItem);
                    }
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized java.util.List<CartItem> getAllCartItems() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createQuery("SELECT c FROM CartItem c", CartItem.class);
                return query.getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    }
}
