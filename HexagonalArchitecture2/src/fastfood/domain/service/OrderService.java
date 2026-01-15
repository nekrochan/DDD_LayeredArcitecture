package fastfood.domain.service;

import fastfood.domain.model.*;
import fastfood.domain.port.primary.OrderManagementUseCase;
import fastfood.domain.port.secondary.NotificationService;
import fastfood.domain.port.secondary.OrderRepository;
import fastfood.domain.port.secondary.ProductRepository;
import fastfood.domain.port.secondary.SupplierRepository;

import java.util.List;
import java.util.Optional;

public class OrderService implements OrderManagementUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final NotificationService notificationService;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.notificationService = notificationService;
    }

    @Override
    public String createOrder(String supplierId) {
        Supplier supplier = getSupplierOrThrow(supplierId);
        Order newOrder = new Order(supplier);
        orderRepository.save(newOrder);
        return newOrder.getId();
    }

    @Override
    public void addProductToOrder(String orderId, String productId, double quantity) {
        Order order = getOrderOrThrow(orderId);
        Product product = getProductOrThrow(productId);

        if (quantity <= 0) {
            throw new IllegalArgumentException("количество должно быть положительным");
        }

        order.addItem(product, quantity);
        orderRepository.save(order);
    }

    @Override
    public void removeProductFromOrder(String orderId, int itemIndex) {
        Order order = getOrderOrThrow(orderId);

        order.removeItem(itemIndex);
        orderRepository.save(order);
    }

    @Override
    public void changeOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = getOrderOrThrow(orderId);

        order.changeStatus(newStatus);
        orderRepository.save(order);

        // отправка уведомления о смене статуса
        notificationService.notifyOrderStatusChanged(order);

        // дополнительное уведомление поставщику при отправке заказа
        if (newStatus == OrderStatus.SENT_TO_SUPPLIER) {
            notificationService.notifySupplier(
                    order.getSupplier().getName(),
                    "получен новый заказ №" + orderId
            );
        }
    }

    @Override
    public void sendOrderToSupplier(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.CREATED &&
                order.getStatus() != OrderStatus.DRAFT) {
            throw new IllegalStateException("заказ можно отправить только из статусов CREATED или DRAFT");
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("нельзя отправить пустой заказ поставщику");
        }

        order.changeStatus(OrderStatus.SENT_TO_SUPPLIER);
        orderRepository.save(order);

        notificationService.notifySupplier(
                order.getSupplier().getName(),
                "заказ №" + orderId + " отправлен на обработку"
        );
    }

    @Override
    public void confirmDelivery(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.SENT_TO_SUPPLIER &&
                order.getStatus() != OrderStatus.IN_TRANSIT) {
            throw new IllegalStateException("доставку можно подтвердить только для отправленных заказов");
        }

        order.changeStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        notificationService.notifySupplier(
                order.getSupplier().getName(),
                "заказ №" + orderId + " получен и подтвержден"
        );
    }

    @Override
    public void acceptProducts(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("качество можно проверять только для доставленных заказов");
        }

        order.changeStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);

        System.out.println("продукты из заказа №" + orderId + " приняты на склад");
    }

    @Override
    public void rejectProducts(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("качество можно проверять только для доставленных заказов");
        }

        order.changeStatus(OrderStatus.REJECTED);
        orderRepository.save(order);

        notificationService.notifySupplier(
                order.getSupplier().getName(),
                "заказ №" + orderId + " отклонен из-за проблем с качеством, требуется возврат или замена"
        );
    }

    @Override
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Product> getAvailableProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> getSupplier(String supplierId) {
        return supplierRepository.findById(supplierId);
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    private Order getOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("заказ не найден: " + orderId));
    }

    private Product getProductOrThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("продукт не найден: " + productId));
    }

    private Supplier getSupplierOrThrow(String supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("поставщик не найден: " + supplierId));
    }
}