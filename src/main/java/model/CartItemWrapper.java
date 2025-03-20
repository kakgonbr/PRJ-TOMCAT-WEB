package model;

public class CartItemWrapper implements java.io.Serializable {
    private Integer id;
    private ProductWrapper productWrapper;
    private ProductItemWrapper productItem;
    private int quantity;

    public CartItemWrapper() {}

    public CartItemWrapper(CartItem cartItem) {
        setId(cartItem.getId());
        setQuantity(cartItem.getQuantity());
        setProductItem(new ProductItemWrapper(cartItem.getProductItemId()));
        setProductWrapper(new ProductWrapper(cartItem.getProductItemId().getProductId()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public ProductWrapper getProductWrapper() {
        return productWrapper;
    }

    public void setProductWrapper(ProductWrapper productWrapper) {
        this.productWrapper = productWrapper;
    }

    public ProductItemWrapper getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItemWrapper productItem) {
        this.productItem = productItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
