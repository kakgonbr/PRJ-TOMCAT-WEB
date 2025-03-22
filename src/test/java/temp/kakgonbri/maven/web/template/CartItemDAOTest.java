package temp.kakgonbri.maven.web.template;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dao.CartItemDAO;
import model.CartItem;
import model.Cart;
import model.ProductItem;

import java.sql.SQLException;
import java.util.List;

public class CartItemDAOTest {

    private CartItemDAO cartItemDAO;

    @BeforeEach
    public void setUp() {
        cartItemDAO = new CartItemDAO();
    }

    @Test
    public void testCreateCartItem() throws SQLException {
        CartItem newItem = new CartItem();
        newItem.setQuantity(2);

        // Giả sử bạn đã có một Cart và ProductItem để liên kết
        Cart cart = new Cart(); // Thay thế bằng cách lấy Cart thực tế
        ProductItem productItem = new ProductItem(); // Thay thế bằng cách lấy ProductItem thực tế

        cart.setId(1); // Giả sử ID của Cart là 1
        productItem.setId(1); // Giả sử ID của ProductItem là 1

        newItem.setCartId(cart);
        newItem.setProductItemId(productItem);

        cartItemDAO.createCartItem(newItem);

        List<CartItem> items = cartItemDAO.getAllItemsByCartId(cart.getId());
        assertTrue(items.stream().anyMatch(item -> item.getQuantity() == 2));
    }

    @Test
    public void testUpdateCartItem() throws SQLException {
        CartItem itemToUpdate = cartItemDAO.getItemById(1); // Thay thế bằng ID thực tế
        itemToUpdate.setQuantity(3);
        cartItemDAO.updateCartItem(itemToUpdate);

        CartItem updatedItem = cartItemDAO.getItemById(1); // Thay thế bằng ID thực tế
        assertEquals(3, updatedItem.getQuantity());
    }

    @Test
    public void testDeleteCartItem() throws SQLException {
        cartItemDAO.deleteCartItem(1); // Thay thế bằng ID thực tế
        CartItem deletedItem = cartItemDAO.getItemById(1); // Thay thế bằng ID thực tế
        assertNull(deletedItem);
    }

    @Test
    public void testGetAllItemsByCartId() throws SQLException {
        List<CartItem> items = cartItemDAO.getAllItemsByCartId(1); // Thay thế bằng cartId thực tế
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void testGetItemById() throws SQLException {
        CartItem item = cartItemDAO.getItemById(1); // Thay thế bằng ID thực tế
        assertNotNull(item);
        assertEquals(1, item.getId());
    }
}