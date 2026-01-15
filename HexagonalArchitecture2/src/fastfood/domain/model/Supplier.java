package fastfood.domain.model;

public class Supplier {
    private String id;
    private String name;
    private String contactPhone;
    private String email;
    private String address;
    private SupplierRating rating;

    public Supplier(String id, String name, String contactPhone, String email) {
        this.id = id;
        this.name = name;
        this.contactPhone = contactPhone;
        this.email = email;
        this.address = "Не указан";
        this.rating = SupplierRating.NEW;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SupplierRating getRating() {
        return rating;
    }

    public void setRating(SupplierRating rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("поставщик: %s (ID: %s)\nтелефон: %s\nemail: %s\nадрес: %s\nрейтинг: %s",
                name, id, contactPhone, email, address, rating);
    }
}