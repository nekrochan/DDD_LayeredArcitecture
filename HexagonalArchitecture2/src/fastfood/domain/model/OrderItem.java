package fastfood.domain.model;

public class OrderItem {
    private Product product;
    private double quantity; // в килограммах или штуках

    public OrderItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f %s) = %.2f руб.",
                product.getName(),
                quantity,
                product.getUnit(),
                getTotalPrice());
    }
}