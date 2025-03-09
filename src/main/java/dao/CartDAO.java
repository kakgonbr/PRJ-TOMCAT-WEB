package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.sql.SQLException;
import model.Cart;
import model.CartItem;
import model.ProductItem;
import service.DatabaseConnection;


public final class CartDAO {

    public static final class CartRetriever {
        private static final String GET_CART = "SELECT c FROM Cart c WHERE c.userId.id = :userID";
        public static synchronized Cart getCart(int userID) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createQuery(GET_CART, Cart.class)
                        .setParameter("userID", userID)
                        .getSingleResult();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
    }

    public static final class CartPaymentProcessor {
        public CartPaymentProcessor() {
        }
    }

    public static final class CartManager {

        public static synchronized boolean addProduct(ProductItem product, int quantity) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    CartItem cartItem = new CartItem();
                    cartItem.setProductItemId(product);
                    cartItem.setQuantity(quantity);
                    em.persist(cartItem);
                    et.commit();
                    return true;
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

        public static synchronized boolean changeCount(int index, int quantity) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    CartItem cartItem = em.find(CartItem.class, index);
                    if (cartItem != null) {
                        cartItem.setQuantity(quantity);
                        et.commit();
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }
    }
}
