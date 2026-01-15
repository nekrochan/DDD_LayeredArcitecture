package fastfood.domain.port.primary;

import fastfood.domain.model.*;
import java.util.List;
import java.util.Optional;

public interface OrderManagementUseCase {

    String createOrder(String supplierId);
    void addProductToOrder(String orderId, String productId, double quantity);
    void removeProductFromOrder(String orderId, int itemIndex);
    void changeOrderStatus(String orderId, OrderStatus newStatus);

    void sendOrderToSupplier(String orderId);
    void confirmDelivery(String orderId);
    void acceptProducts(String orderId);
    void rejectProducts(String orderId);

    Optional<Order> getOrder(String orderId);
    List<Order> getAllOrders();
    List<Product> getAvailableProducts();
    List<Supplier> getAllSuppliers();
    Optional<Supplier> getSupplier(String supplierId);
    List<Product> getProductsByCategory(ProductCategory category);
}