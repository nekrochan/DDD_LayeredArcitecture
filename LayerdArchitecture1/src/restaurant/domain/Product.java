package restaurant.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Product {
    private String id;
    private String name;
    private String category;
    private double quantity;
    private String unit; // кг, литры, штуки и т.д.
    private LocalDate expiryDate;
    private double minStockLevel;
    private double criticalStockLevel;
    private boolean isExpired;

    public Product(String id, String name, String category, double quantity,
                   String unit, LocalDate expiryDate, double minStockLevel,
                   double criticalStockLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.minStockLevel = minStockLevel;
        this.criticalStockLevel = criticalStockLevel;
        this.isExpired = checkIfExpired();
    }

    public Product(Product other, LocalDate newExpiryDate, double newQuantity) {
        this.id = UUID.randomUUID().toString().substring(0,5);
        this.name = other.name;
        this.category = other.category;
        this.quantity = newQuantity;
        this.unit = other.unit;
        this.expiryDate = newExpiryDate;
        this.minStockLevel = other.minStockLevel;
        this.criticalStockLevel = other.criticalStockLevel;
        this.isExpired = checkIfExpired();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getMinStockLevel() {
        return minStockLevel;
    }

    public double getCriticalStockLevel() {
        return criticalStockLevel;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public boolean isBelowCriticalLevel() {
        return quantity <= criticalStockLevel;
    }

    public boolean isBelowMinLevel() {
        return quantity <= minStockLevel;
    }

    public void addQuantity(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        this.quantity += amount;
    }

    public void useQuantity(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        if (amount > quantity) {
            throw new IllegalStateException("Недостаточно продукта на складе");
        }
        this.quantity -= amount;
        updateExpirationStatus();
    }

    public void writeOff() {
        this.quantity = 0;
        this.isExpired = true;
    }

    public void adjustQuantity(double newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }
        this.quantity = newQuantity;
        updateExpirationStatus();
    }

    private boolean checkIfExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    private void updateExpirationStatus() {
        this.isExpired = checkIfExpired();
    }

    @Override
    public String toString() {
        String status;
        if (quantity>0 & isExpired) {
            status = "ПРОСРОЧЕН";
        } else if (isBelowCriticalLevel()) {
            status = "КРИТИЧЕСКИЙ УРОВЕНЬ";
        } else {
            status = "OK";
        }

        return String.format(
                "Продукт: %s (ID: %s) - %.2f %s, Срок годности: %s, %s",
                name, id, quantity, unit, expiryDate, status
        );
    }
}