package restaurant.presentation;

import restaurant.application.InventoryService;
import restaurant.domain.Inventory;
import restaurant.domain.Product;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class InventoryConsoleUI {
    private final InventoryService inventoryService;
    private final Scanner scanner;
    private String currentRestaurantName;

    public InventoryConsoleUI(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.scanner = new Scanner(System.in);
        this.currentRestaurantName = "Kostroma Fried Chicken"; // ID текущего ресторана
    }

    public void start() {
        int choice = -1;
        do {
            showMenu();
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Ошибка: Неверный тип введенных данных");
            }
            scanner.nextLine(); // очистка буфера
            handleMenuChoice(choice);
        } while (choice != 0);
    }

    private void showMenu() {
        System.out.println("\n===== СИСТЕМА УПРАВЛЕНИЯ ИНВЕНТАРЕМ =====");
        System.out.println("Ресторан: " + currentRestaurantName);
        System.out.println("1. Добавить новый продукт");
        System.out.println("2. Принять поставку продукта");
        System.out.println("3. Использовать продукт для приготовления");
        System.out.println("4. Списать просроченный продукт");
        System.out.println("5. Провести инвентаризацию");
        System.out.println("6. Корректировать количество продукта");
        System.out.println("7. Показать все продукты");
        System.out.println("8. Показать просроченные продукты");
        System.out.println("9. Показать продукты с критическим уровнем");
        System.out.println("10. Показать продукты ниже минимального уровня");
        System.out.println("11. Показать информацию об инвентаре");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                try {
                    addProduct();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    receiveProduct();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    useProduct();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 4:
                try {
                    writeOffProduct();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 5:
                try {
                    conductInventoryCheck();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 6:
                try {
                    adjustInventory();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 7:
                try {
                    showAllProducts();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 8:
                try {
                    showExpiredProducts();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 9:
                try {
                    showCriticalLevelProducts();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 10:
                try {
                    showBelowMinLevelProducts();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 11:
                try {
                    showInventoryInfo();
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                break;
            case 0:
                System.out.println("Выход из программы...");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private void addProduct() {
        System.out.println("\n=== ДОБАВЛЕНИЕ НОВОГО ПРОДУКТА ===");

        System.out.print("Введите название продукта: ");
        String name = scanner.nextLine();

        System.out.print("Введите категорию (мясо, овощи, молочные и т.д.): ");
        String category = scanner.nextLine();

        System.out.print("Введите начальное количество: ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Введите единицу измерения (кг, л, шт и т.д.): ");
        String unit = scanner.nextLine();

        System.out.print("Введите срок годности (ГГГГ-ММ-ДД): ");
        LocalDate expiryDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Введите минимальный уровень запаса: ");
        double minStockLevel = scanner.nextDouble();

        System.out.print("Введите критический уровень запаса: ");
        double criticalStockLevel = scanner.nextDouble();
        scanner.nextLine();

        try {
            inventoryService.addProduct(name, category, quantity, unit,
                    expiryDate, minStockLevel, criticalStockLevel,
                    currentRestaurantName);
            System.out.println("Продукт успешно добавлен в инвентарь.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении продукта: " + e.getMessage());
        }
    }

    private void receiveProduct() {
        System.out.println("\n=== ПРИЕМ ПОСТАВКИ ПРОДУКТА ===");

        System.out.print("Введите название продукта: ");
        String productName = scanner.nextLine();

        System.out.print("Введите количество для приема: ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Введите срок годности (ГГГГ-ММ-ДД): ");
        LocalDate expiryDate;
        try {
            expiryDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД");
            return;
        }

        try {
            inventoryService.receiveProduct(productName, quantity, expiryDate, currentRestaurantName);
            System.out.println("Продукт успешно принят.");
        } catch (Exception e) {
            System.err.println("Ошибка при приеме продукта: " + e.getMessage());
        }
    }

    private void useProduct() {
        System.out.println("\n=== ИСПОЛЬЗОВАНИЕ ПРОДУКТА ===");

        System.out.print("Введите ID продукта: ");
        String productId = scanner.nextLine();

        System.out.print("Введите количество для использования: ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();

        try {
            inventoryService.useProduct(productId, quantity, currentRestaurantName);
            System.out.println("Продукт успешно использован.");
        } catch (Exception e) {
            System.out.println("Ошибка при использовании продукта: " + e.getMessage());
        }
    }

    private void writeOffProduct() {
        System.out.println("\n=== СПИСАНИЕ ПРОСРОЧЕННОГО ПРОДУКТА ===");

        System.out.print("Введите ID продукта: ");
        String productId = scanner.nextLine();

        try {
            inventoryService.writeOffExpiredProduct(productId, currentRestaurantName);
            System.out.println("Продукт успешно списан.");
        } catch (Exception e) {
            System.out.println("Ошибка при списании продукта: " + e.getMessage());
        }
    }

    private void conductInventoryCheck() {
        System.out.println("\n=== ПРОВЕДЕНИЕ ИНВЕНТАРИЗАЦИИ ===");

        try {
            inventoryService.conductInventoryCheck(currentRestaurantName);
        } catch (Exception e) {
            System.out.println("Ошибка при проведении инвентаризации: " + e.getMessage());
        }
    }

    private void adjustInventory() {
        System.out.println("\n=== КОРРЕКТИРОВКА ЗАПАСОВ ===");

        System.out.print("Введите ID продукта: ");
        String productId = scanner.nextLine();

        System.out.print("Введите новое количество: ");
        double newQuantity = scanner.nextDouble();
        scanner.nextLine();

        try {
            inventoryService.adjustInventory(productId, newQuantity, currentRestaurantName);
            System.out.println("Запасы успешно скорректированы.");
        } catch (Exception e) {
            System.out.println("Ошибка при корректировке запасов: " + e.getMessage());
        }
    }

    private void showAllProducts() {
        List<Product> products = inventoryService.getAllProducts(currentRestaurantName);

        if (products.isEmpty()) {
            System.out.println("В инвентаре нет продуктов.");
            return;
        }

        System.out.println("\n=== ВСЕ ПРОДУКТЫ ===");
        System.out.printf("%-5s %-20s %-10s %-10s %-15s %-15s%n",
                "ID", "Название", "Кол-во", "Ед.", "Срок годности", "Статус");
        System.out.println("-".repeat(80));

        for (Product product : products) {
            String status;
            if (product.isExpired() & product.getQuantity()>0) {
                status = "ПРОСРОЧЕН";
            } else if (product.isBelowCriticalLevel()) {
                status = "КРИТИЧЕСКИЙ";
            } else if (product.isBelowMinLevel()) {
                status = "НИЖЕ МИН.";
            } else {
                status = "OK";
            }

            System.out.printf("%-5s %-20s %-10.2f %-10s %-15s %-15s%n",
                    product.getId(), product.getName(),
                    product.getQuantity(), product.getUnit(),
                    product.getExpiryDate(), status);
        }
    }

    private void showExpiredProducts() {
        List<Product> expiredProducts = inventoryService.getExpiredProducts(currentRestaurantName);

        if (expiredProducts.isEmpty()) {
            System.out.println("Просроченных продуктов нет.");
            return;
        }

        System.out.println("\n=== ПРОСРОЧЕННЫЕ ПРОДУКТЫ ===");
        for (Product product : expiredProducts) {
            System.out.println(product);
        }
        System.out.println("Всего просроченных продуктов: " + expiredProducts.size());
    }

    private void showCriticalLevelProducts() {
        List<Product> criticalProducts = inventoryService.getCriticalLevelProducts(currentRestaurantName);

        if (criticalProducts.isEmpty()) {
            System.out.println("Продуктов с критическим уровнем нет.");
            return;
        }

        System.out.println("\n=== ПРОДУКТЫ С КРИТИЧЕСКИМ УРОВНЕМ ===");
        for (Product product : criticalProducts) {
            System.out.println(product);
        }
        System.out.println("Требуется срочное пополнение: " + criticalProducts.size() + " продуктов");
    }

    private void showBelowMinLevelProducts() {
        List<Product> belowMinProducts = inventoryService.getBelowMinLevelProducts(currentRestaurantName);

        if (belowMinProducts.isEmpty()) {
            System.out.println("Все продукты выше минимального уровня.");
            return;
        }

        System.out.println("\n=== ПРОДУКТЫ НИЖЕ МИНИМАЛЬНОГО УРОВНЯ ===");
        for (Product product : belowMinProducts) {
            System.out.println(product);
        }
    }

    private void showInventoryInfo() {
        Optional<Inventory> inventoryOpt = inventoryService.getInventory(currentRestaurantName);

        if (!inventoryOpt.isPresent()) {
            System.out.println("Инвентарь для ресторана не найден.");
            return;
        }

        Inventory inventory = inventoryOpt.get();

        System.out.println("\n=== ИНФОРМАЦИЯ ОБ ИНВЕНТАРЕ ===");
        System.out.println("Ресторан ID: " + inventory.getRestaurantName());
        System.out.println("Всего продуктов: " + inventory.getAllProducts().size());
        System.out.println("Просроченных продуктов: " + inventory.getExpiredProducts().size());
        System.out.println("Продуктов с критическим уровнем: " + inventory.getCriticalLevelProducts().size());
        System.out.println("Продуктов ниже минимального уровня: " + inventory.getBelowMinLevelProducts().size());
        System.out.printf("Общее количество (всех единиц): %.2f%n", inventory.getTotalInventoryValue());
    }
}