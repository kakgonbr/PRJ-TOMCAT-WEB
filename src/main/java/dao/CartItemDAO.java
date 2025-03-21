package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.CartItem;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartItemDAO {
    private static final Logger logger = Logger.getLogger(CartItemDAO.class.getName());

    public static class CartItemManager {
        public static synchronized void createCartItem(CartItem cartItem) throws java.sql.SQLException {
            logger.info("Creating cart item: " + cartItem);
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    em.persist(cartItem);
                    et.commit();
                    logger.info("Cart item created successfully.");
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logger.log(Level.SEVERE, "Failed to create cart item", e);
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized CartItem getCartItem(int id) throws java.sql.SQLException {
            logger.info("Fetching cart item with ID: " + id);
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                CartItem cartItem = em.find(CartItem.class, id);
                logger.info("Fetched cart item: " + cartItem);
                return cartItem;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to fetch cart item", e);
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized void updateCartItem(CartItem cartItem) throws java.sql.SQLException {
            logger.info("Updating cart item: " + cartItem);
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    em.merge(cartItem);
                    et.commit();
                    logger.info("Cart item updated successfully.");
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logger.log(Level.SEVERE, "Failed to update cart item", e);
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized void deleteCartItem(int id) throws java.sql.SQLException {
            logger.info("Deleting cart item with ID: " + id);
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    CartItem cartItem = em.find(CartItem.class, id);
                    if (cartItem != null) {
                        em.remove(cartItem);
                        logger.info("Cart item deleted successfully.");
                    } else {
                        logger.warning("Cart item not found for ID: " + id);
                    }
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logger.log(Level.SEVERE, "Failed to delete cart item", e);
                    throw new java.sql.SQLException(e);
                }
            }
        }

        public static synchronized java.util.List<CartItem> getAllCartItems() throws java.sql.SQLException {
            logger.info("Fetching all cart items.");
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createQuery("SELECT c FROM CartItem c", CartItem.class);
                java.util.List<CartItem> cartItems = query.getResultList();
                logger.info("Fetched " + cartItems.size() + " cart items.");
                return cartItems;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to fetch cart items", e);
                throw new java.sql.SQLException(e);
            }
        }
    }
}
