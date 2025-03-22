package dao;

import model.CartItem;
import model.Cart;
import model.ProductItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;

public class CartItemDAO {

    private EntityManager entityManager;

    public CartItemDAO() {
        entityManager = Persistence.createEntityManagerFactory("YourPersistenceUnitName").createEntityManager();
    }

    public void createCartItem(CartItem cartItem) throws SQLException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(cartItem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Error creating CartItem: " + e.getMessage());
        }
    }

    public CartItem getItemById(Integer id) {
        return entityManager.find(CartItem.class, id);
    }

    public List<CartItem> getAllItemsByCartId(Integer cartId) {
        return entityManager.createNamedQuery("CartItem.findByCartId", CartItem.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public void updateCartItem(CartItem cartItem) throws SQLException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(cartItem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Error updating CartItem: " + e.getMessage());
        }
    }

    public void deleteCartItem(Integer id) throws SQLException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            CartItem cartItem = getItemById(id);
            if (cartItem != null) {
                entityManager.remove(cartItem);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Error deleting CartItem: " + e.getMessage());
        }
    }
}