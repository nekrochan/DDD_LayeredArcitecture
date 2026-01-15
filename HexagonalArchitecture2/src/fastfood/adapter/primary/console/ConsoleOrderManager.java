package fastfood.adapter.primary.console;

import fastfood.domain.model.*;
import fastfood.domain.port.primary.OrderManagementUseCase;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleOrderManager {
    private final OrderManagementUseCase orderManagement;
    private final Scanner scanner;
    private String currentOrderId;

    public ConsoleOrderManager(OrderManagementUseCase orderManagement) {
        this.orderManagement = orderManagement;
        this.scanner = new Scanner(System.in);
        this.currentOrderId = null;
    }

    public void start() {
        int choice;
        do {
            showMainMenu();
            choice = readIntInput();
            scanner.nextLine();
            handleMainMenuChoice(choice);
        } while (choice != 0);
    }

    private void showMainMenu() {
        System.out.println("\n===== FastFood Network =====");
        if (currentOrderId != null) {
            System.out.println("текущий заказ поставщику: №" + currentOrderId);
        }
        System.out.println("1. создать новый заказ поставщику");
        System.out.println("2. выбрать существующий заказ поставщику");
        System.out.println("3. показать все заказы поставщикам");
        System.out.println("4. показать всех поставщиков");
        System.out.println("5. показать все продукты");

        if (currentOrderId != null) {
            System.out.println("6. добавить продукт в текущий заказ");
            System.out.println("7. удалить продукт из текущего заказа");
            System.out.println("8. изменить статус текущего заказа");
            System.out.println("9. показать детали текущего заказа");
            System.out.println("10. отправить заказ поставщику");
            System.out.println("11. подтвердить доставку заказа");
            System.out.println("12. проверить качество поставки");
        }

        System.out.println("0. выход");
        System.out.print("выберите действие: ");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                System.out.println("завершение работы");
                break;
            case 1:
                createNewOrder();
                break;
            case 2:
                selectExistingOrder();
                break;
            case 3:
                showAllOrders();
                break;
            case 4:
                showAllSuppliers();
                break;
            case 5:
                showAllProducts();
                break;
            case 6:
                if (currentOrderId != null) addProductToOrder();
                else showNoOrderSelectedMessage();
                break;
            case 7:
                if (currentOrderId != null) removeProductFromOrder();
                else showNoOrderSelectedMessage();
                break;
            case 8:
                if (currentOrderId != null) changeOrderStatus();
                else showNoOrderSelectedMessage();
                break;
            case 9:
                if (currentOrderId != null) showOrderDetails();
                else showNoOrderSelectedMessage();
                break;
            case 10:
                if (currentOrderId != null) sendOrderToSupplier();
                else showNoOrderSelectedMessage();
                break;
            case 11:
                if (currentOrderId != null) confirmDelivery();
                else showNoOrderSelectedMessage();
                break;
            case 12:
                if (currentOrderId != null) checkQuality();
                else showNoOrderSelectedMessage();
                break;
            default:
                System.out.println("неверный выбор, попробуйте снова");
        }
    }

    private void createNewOrder() {
        List<Supplier> suppliers = orderManagement.getAllSuppliers();

        if (suppliers.isEmpty()) {
            System.out.println("нет доступных поставщиков");
            return;
        }

        System.out.println("\nвыберите поставщика:");
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            System.out.println((i + 1) + ". " + supplier.getName() + " (" + supplier.getContactPhone() + ")");
        }

        System.out.print("выберите поставщика (1-" + suppliers.size() + "): ");
        int supplierIndex = readIntInput() - 1;
        scanner.nextLine();

        if (supplierIndex < 0 || supplierIndex >= suppliers.size()) {
            System.out.println("некорректный выбор поставщика");
            return;
        }

        String supplierId = suppliers.get(supplierIndex).getId();
        currentOrderId = orderManagement.createOrder(supplierId);
        System.out.println("создан новый заказ поставщику №" + currentOrderId);
    }

    private void selectExistingOrder() {
        List<Order> orders = orderManagement.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("нет доступных заказов");
            return;
        }

        System.out.println("\nдоступные заказы:");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println("\t" + (i + 1) + ". заказ №" + order.getId() +
                    " - " + order.getSupplier().getName() + " (" + order.getStatus() + ")");
        }

        System.out.print("выберите номер заказа (1-" + orders.size() + "): ");
        int orderIndex = readIntInput() - 1;
        scanner.nextLine();

        if (orderIndex >= 0 && orderIndex < orders.size()) {
            currentOrderId = orders.get(orderIndex).getId();
            System.out.println("выбран заказ №" + currentOrderId);
        } else {
            System.out.println("некорректный выбор заказа");
        }
    }

    private void showAllOrders() {
        List<Order> orders = orderManagement.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("нет доступных заказов");
            return;
        }

        System.out.println("\nвсе заказы поставщикам:");
        System.out.println("***********************");
        for (Order order : orders) {
            System.out.println("\t" + "заказ №" + order.getId());
            System.out.println("\t\t" + "поставщик: " + order.getSupplier().getName());
            System.out.println("\t\t" + "статус: " + order.getStatus());
            System.out.println("\t\t" + "сумма: " + order.calculateTotalPrice() + " руб");
            System.out.println("\t\t" + "продукты: " + order.getItems().size());
            System.out.println("\t\t" + "дата создания: " + order.getCreatedAt());
            System.out.println("-----------------------");
        }
    }

    private void showAllSuppliers() {
        List<Supplier> suppliers = orderManagement.getAllSuppliers();

        if (suppliers.isEmpty()) {
            System.out.println("нет доступных поставщиков");
            return;
        }

        System.out.println("\nвсе поставщики:");
        for (Supplier supplier : suppliers) {
            System.out.println("---\n" + supplier);
        }
    }

    private void showAllProducts() {
        List<Product> products = orderManagement.getAvailableProducts();

        if (products.isEmpty()) {
            System.out.println("нет доступных продуктов");
            return;
        }

        System.out.println("\nвсе продукты:");
        for (Product product : products) {
            System.out.println("\t" + product);
        }
    }

    private void addProductToOrder() {
        List<Product> products = orderManagement.getAvailableProducts();

        if (products.isEmpty()) {
            System.out.println("нет доступных продуктов");
            return;
        }

        System.out.println("\nдоступные продукты:");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println("\t" + (i + 1) + ". " + product);
        }

        System.out.print("выберите номер продукта (1-" + products.size() + "): ");
        int productIndex = readIntInput() - 1;
        scanner.nextLine();

        if (productIndex < 0 || productIndex >= products.size()) {
            System.out.println("некорректный выбор продукта");
            return;
        }

        System.out.print("введите количество (кг/шт): ");
        double quantity = readDoubleInput();
        scanner.nextLine();

        try {
            String productId = products.get(productIndex).getId();
            orderManagement.addProductToOrder(currentOrderId, productId, quantity);
            System.out.println("продукт успешно добавлен в заказ");
        } catch (Exception e) {
            System.out.println("ошибка при добавлении продукта: " + e.getMessage());
        }
    }

    private void removeProductFromOrder() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("заказ не найден");
            return;
        }

        Order order = orderOpt.get();

        if (order.getItems().isEmpty()) {
            System.out.println("в заказе нет продуктов");
            return;
        }

        System.out.println("\nпродукты в заказе:");
        for (int i = 0; i < order.getItems().size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + order.getItems().get(i));
        }

        System.out.print("выберите номер продукта для удаления (1-" + order.getItems().size() + "): ");
        int itemIndex = readIntInput() - 1;
        scanner.nextLine();

        try {
            orderManagement.removeProductFromOrder(currentOrderId, itemIndex);
            System.out.println("продукт успешно удален из заказа");
        } catch (Exception e) {
            System.out.println("ошибка при удалении продукта: " + e.getMessage());
        }
    }

    private void changeOrderStatus() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("заказ не найден");
            return;
        }

        Order order = orderOpt.get();
        OrderStatus currentStatus = order.getStatus();

        System.out.println("\nтекущий статус заказа: " + currentStatus);

        OrderStatus[] availableStatuses = OrderStatus.values();
        System.out.println("доступные статусы:");
        for (int i = 0; i < availableStatuses.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + availableStatuses[i]);
        }

        System.out.print("выберите новый статус (1-" + availableStatuses.length + "): ");
        int statusIndex = readIntInput() - 1;
        scanner.nextLine();

        if (statusIndex < 0 || statusIndex >= availableStatuses.length) {
            System.out.println("некорректный выбор статуса");
            return;
        }

        OrderStatus newStatus = availableStatuses[statusIndex];

        try {
            orderManagement.changeOrderStatus(currentOrderId, newStatus);
            System.out.println("статус заказа успешно изменен на: " + newStatus);
        } catch (Exception e) {
            System.out.println("ошибка при изменении статуса: " + e.getMessage());
        }
    }

    private void sendOrderToSupplier() {
        try {
            orderManagement.sendOrderToSupplier(currentOrderId);
            System.out.println("заказ успешно отправлен поставщику");
        } catch (Exception e) {
            System.out.println("ошибка при отправке заказа: " + e.getMessage());
        }
    }

    private void confirmDelivery() {
        try {
            orderManagement.confirmDelivery(currentOrderId);
            System.out.println("доставка подтверждена");
        } catch (Exception e) {
            System.out.println("ошибка при подтверждении доставки: " + e.getMessage());
        }
    }

    private void checkQuality() {
        System.out.println("результат проверки качества:");
        System.out.println("1. продукты приняты");
        System.out.println("2. продукты отклонены (проблемы с качеством)");
        System.out.print("выберите результат: ");

        int choice = readIntInput();
        scanner.nextLine();

        if (choice == 1) {
            try {
                orderManagement.acceptProducts(currentOrderId);
                System.out.println("продукты приняты по качеству");
            } catch (Exception e) {
                System.out.println("ошибка: " + e.getMessage());
            }
        } else if (choice == 2) {
            try {
                orderManagement.rejectProducts(currentOrderId);
                System.out.println("продукты отклонены из-за проблем с качеством");
            } catch (Exception e) {
                System.out.println("ошибка: " + e.getMessage());
            }
        } else {
            System.out.println("некорректный выбор");
        }
    }

    private void showOrderDetails() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("заказ не найден");
            return;
        }

        Order order = orderOpt.get();
        System.out.println("\n" + order);
    }

    private void showNoOrderSelectedMessage() {
        System.out.println("сначала необходимо создать или выбрать заказ");
    }

    private int readIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDoubleInput() {
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return -1;
        }
    }
}