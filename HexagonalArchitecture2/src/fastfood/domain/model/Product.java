package fastfood.domain.model;

import java.time.LocalDate;

public class Product {
    private String id;
    private String name;
    private double price; // цена за единицу
    private ProductCategory category;
    private LocalDate expiryDate;
    private String unit; // единица измерения (кг, шт, л)
    private TemperatureMode temperatureMode;

    public Product(String id, String name, double price, ProductCategory category, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.expiryDate = expiryDate;
        this.unit = determineUnitByCategory(category);
        this.temperatureMode = determineTemperatureModeByCategory(category);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getUnit() {
        return unit;
    }

    public TemperatureMode getTemperatureMode() {
        return temperatureMode;
    }

    private String determineUnitByCategory(ProductCategory category) {
        switch (category) {
            case MEAT:
            case VEGETABLES:
                return "кг";
            case BAKERY:
            case DAIRY:
                return "шт";
            case SAUCES:
                return "л";
            default:
                return "шт";
        }
    }

    private TemperatureMode determineTemperatureModeByCategory(ProductCategory category) {
        switch (category) {
            case MEAT:
                return TemperatureMode.FROZEN;
            case DAIRY:
            case VEGETABLES:
                return TemperatureMode.REFRIGERATED;
            case BAKERY:
            case SAUCES:
                return TemperatureMode.ROOM_TEMPERATURE;
            default:
                return TemperatureMode.ROOM_TEMPERATURE;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) - %.2f руб./%s [категория: %s, срок годности: %s, хранение: %s]",
                name, id, price, unit, category, expiryDate, temperatureMode);
    }
}