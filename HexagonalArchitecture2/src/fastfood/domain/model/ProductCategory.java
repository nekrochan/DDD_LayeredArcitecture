package fastfood.domain.model;

public enum ProductCategory {
    MEAT("мясо"),
    VEGETABLES("овощи"),
    DAIRY("молочные продукты"),
    BAKERY("выпечка"),
    SAUCES("соусы"),
    BEVERAGES("напитки"),
    OTHER("другое");

    private String description;

    ProductCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}