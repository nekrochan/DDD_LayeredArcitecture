package fastfood.domain.model;

public enum SupplierRating {
    NEW("новый"),
    GOOD("хороший"),
    EXCELLENT("отличный"),
    PROBLEMATIC("проблемный"),
    BLACKLISTED("чс");

    private String description;

    SupplierRating(String description) {
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