package restaurant.application;

import restaurant.domain.*;
import restaurant.domain.Inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InventoryService {
    private final IProductRepository productRepository;
    private final IInventoryRepository inventoryRepository;

    public InventoryService(IProductRepository productRepository,
                            IInventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void addProduct(String name, String category, double initialQuantity,
                           String unit, LocalDate expiryDate, double minStockLevel,
                           double criticalStockLevel, String restaurantName) {
        Product product = new Product(UUID.randomUUID().toString().substring(0,5), name.toLowerCase(), category, initialQuantity, unit,
                expiryDate, minStockLevel, criticalStockLevel);
        productRepository.save(product);

        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElse(new Inventory(restaurantName));
        inventory.addProduct(product);
        inventoryRepository.save(inventory);
    }

    public void receiveProduct(String productName, double quantity, LocalDate expiryDate, String restaurantName) {
        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Инвентарь ресторана не найден"));

        inventory.receiveProduct(productName.toLowerCase(), quantity, expiryDate);
        inventoryRepository.save(inventory);

        for (Product product : inventory.getAllProducts()) {
            productRepository.save(product);
        }
    }

    public void useProduct(String productId, double quantity, String restaurantName) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));

        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Инвентарь ресторана не найден"));

        inventory.useProduct(productId, quantity);
        inventoryRepository.save(inventory);

        if (product.isBelowCriticalLevel()) {
            System.out.println("ВНИМАНИЕ: Критический уровень запаса продукта: " + product.getName());
        }
    }

    public void writeOffExpiredProduct(String productId, String restaurantName) {
        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Инвентарь ресторана не найден"));

        inventory.writeOffExpiredProduct(productId);
        inventoryRepository.save(inventory);
    }

    public void adjustInventory(String productId, double newQuantity, String restaurantName) {
        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Инвентарь ресторана не найден"));

        inventory.adjustProductQuantity(productId, newQuantity);
        inventoryRepository.save(inventory);
    }

    public List<Product> getExpiredProducts(String restaurantName) {
        return inventoryRepository.findByRestaurantName(restaurantName)
                .map(Inventory::getExpiredProducts)
                .orElse(List.of());
    }

    public List<Product> getCriticalLevelProducts(String restaurantName) {
        return inventoryRepository.findByRestaurantName(restaurantName)
                .map(Inventory::getCriticalLevelProducts)
                .orElse(List.of());
    }

    public List<Product> getBelowMinLevelProducts(String restaurantName) {
        return inventoryRepository.findByRestaurantName(restaurantName)
                .map(Inventory::getBelowMinLevelProducts)
                .orElse(List.of());
    }

    public List<Product> getAllProducts(String restaurantName) {
        return inventoryRepository.findByRestaurantName(restaurantName)
                .map(Inventory::getAllProducts)
                .orElse(List.of());
    }

    public Optional<Inventory> getInventory(String restaurantName) {
        return inventoryRepository.findByRestaurantName(restaurantName);
    }

    public void conductInventoryCheck(String restaurantName) {
        Inventory inventory = inventoryRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Инвентарь ресторана не найден"));

        System.out.println("=== ИНВЕНТАРИЗАЦИЯ РЕСТОРАНА " + restaurantName + " ===");
        System.out.println("Всего продуктов: " + inventory.getAllProducts().size());
        System.out.println("Просроченных продуктов: " + inventory.getExpiredProducts().size());
        System.out.println("Продуктов с критическим уровнем: " + inventory.getCriticalLevelProducts().size());

        inventory.getExpiredProducts().forEach(product ->
                inventory.writeOffExpiredProduct(product.getId()));

        inventoryRepository.save(inventory);
        System.out.println("Инвентаризация завершена.");
    }
}