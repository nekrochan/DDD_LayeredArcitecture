package restaurant;

import restaurant.application.InventoryService;
import restaurant.domain.IInventoryRepository;
import restaurant.domain.IProductRepository;
import restaurant.infrastructure.InMemoryInventoryRepository;
import restaurant.infrastructure.InMemoryProductRepository;
import restaurant.presentation.InventoryConsoleUI;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        IProductRepository productRepository = new InMemoryProductRepository();
        IInventoryRepository inventoryRepository = new InMemoryInventoryRepository();

        InventoryService inventoryService = new InventoryService(productRepository, inventoryRepository);

        String restaurantName = "Kostroma Fried Chicken";

        inventoryService.addProduct("Колеты", "мясо", 50.0, "кг",
                LocalDate.now().plusDays(7), 20.0, 10.0, restaurantName);

        inventoryService.addProduct("Картофель", "овощи", 100.0, "кг",
                LocalDate.now().plusDays(30), 50.0, 20.0, restaurantName);

        inventoryService.addProduct("Сыр", "молочные", 15.0, "кг",
                LocalDate.now().plusDays(14), 10.0, 5.0, restaurantName);

        inventoryService.addProduct("Помидоры", "овощи", 25.0, "кг",
                LocalDate.now().minusDays(3), 15.0, 8.0, restaurantName);

        InventoryConsoleUI ui = new InventoryConsoleUI(inventoryService);

        ui.start();
    }
}