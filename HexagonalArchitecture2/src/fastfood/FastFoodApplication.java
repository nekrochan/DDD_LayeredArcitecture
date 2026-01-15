package fastfood;

import fastfood.adapter.primary.console.ConsoleOrderManager;
import fastfood.adapter.secondary.notification.ConsoleNotificationService;
import fastfood.adapter.secondary.persistence.InMemoryOrderRepository;
import fastfood.adapter.secondary.persistence.InMemoryProductRepository;
import fastfood.adapter.secondary.persistence.InMemorySupplierRepository;
import fastfood.domain.port.primary.OrderManagementUseCase;
import fastfood.domain.port.secondary.NotificationService;
import fastfood.domain.port.secondary.OrderRepository;
import fastfood.domain.port.secondary.ProductRepository;
import fastfood.domain.port.secondary.SupplierRepository;
import fastfood.domain.service.OrderService;

public class FastFoodApplication {
    public static void main(String[] args) {

        OrderRepository orderRepository = new InMemoryOrderRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        SupplierRepository supplierRepository = new InMemorySupplierRepository();
        NotificationService notificationService = new ConsoleNotificationService();

        OrderManagementUseCase orderManagementService = new OrderService(
                orderRepository,
                productRepository,
                supplierRepository,
                notificationService
        );

        ConsoleOrderManager consoleUI = new ConsoleOrderManager(orderManagementService);

        consoleUI.start();
    }
}