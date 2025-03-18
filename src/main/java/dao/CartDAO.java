package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.sql.SQLException;
import java.util.List;
import model.Cart;

public final class CartDAO {

    public static final class CartManager {

        public static synchronized void createCart(Cart cart) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.persist(cart);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

        private static final String DELETE_CART = "UPDATE tblCart SET status = 0 WHERE id = ?1";

        public static synchronized void deleteCart(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                em.createNativeQuery(DELETE_CART).setParameter(1, id).executeUpdate();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized void updateCart(Cart cart) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();

                    Cart dbCart = em.find(Cart.class, cart.getId());

                    dbCart.setUserId(cart.getUserId());
                    dbCart.setCartItemList(cart.getCartItemList());

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

    }

    public static final class CartFetcher {

        public static synchronized Cart getCart(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Cart.findById", Cart.class)
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<Cart> getCarts() throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Cart.findAll", Cart.class).getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
    }
}
