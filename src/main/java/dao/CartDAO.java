package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.sql.SQLException;
import model.CartItem;
import model.ProductItem;

public final class CartDAO {

    public static final class CartRetriever {

        public static synchronized model.Cart getCart(int userID) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Cart.findById", model.Cart.class).setParameter("userID", userID).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized java.util.List<model.Cart> getCarts()
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Cart.findAll", model.Cart.class).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    }

    public static final class CartPaymentProcessor {

        public CartPaymentProcessor() {
        }
    }

    public static final class CartManager {

        public static synchronized boolean addProduct(ProductItem productItem, int quantity) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    CartItem cartItem = new CartItem();
                    cartItem.setProductItemId(productItem); 
                    cartItem.setQuantity(quantity);
                    cartItem.setStatus(true);
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
                            if (quantity > 0) {
                                cartItem.setQuantity(quantity);
                                em.merge(cartItem);
                            } else {
                                em.remove(cartItem);
                            }
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
