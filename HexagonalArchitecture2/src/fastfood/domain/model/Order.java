package fastfood.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private String id;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Supplier supplier;
    private String deliveryAddress;
    private String notes;

    public Order(Supplier supplier) {
        this.id = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.supplier = supplier;
        this.deliveryAddress = "Центральный склад FastFood Network, г. Москва";
        this.notes = "";
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        this.updatedAt = LocalDateTime.now();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(Product product, double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("количество товара должно быть положительным числом");
        }
        items.add(new OrderItem(product, quantity));
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("некорректный индекс товара");
        }
        items.remove(index);
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(OrderStatus newStatus) {
        // проверка допустимости перехода статуса
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new IllegalStateException("недопустимый переход статуса: " + this.status + " -> " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case CREATED:
                return next == OrderStatus.DRAFT || next == OrderStatus.SENT_TO_SUPPLIER;
            case DRAFT:
                return next == OrderStatus.SENT_TO_SUPPLIER || next == OrderStatus.CANCELLED;
            case SENT_TO_SUPPLIER:
                return next == OrderStatus.IN_TRANSIT || next == OrderStatus.CANCELLED;
            case IN_TRANSIT:
                return next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            case DELIVERED:
                return next == OrderStatus.ACCEPTED || next == OrderStatus.REJECTED;
            case ACCEPTED:
                return false; //финальный статус
            case REJECTED:
                return false; //финальный статус
            case CANCELLED:
                return false; //финальный статус
            default:
                return false;
        }
    }

    public double calculateTotalPrice() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("заказ поставщику №").append(id).append("\n");
        sb.append("------------------------------------\n");
        sb.append("поставщик: ").append(supplier.getName()).append("\n");
        sb.append("статус: ").append(status).append("\n");
        sb.append("дата создания: ").append(createdAt).append("\n");
        sb.append("дата обновления: ").append(updatedAt).append("\n");
        sb.append("адрес доставки: ").append(deliveryAddress).append("\n");

        if (!notes.isEmpty()) {
            sb.append("примечания: ").append(notes).append("\n");
        }

        sb.append("\nпозиции заказа:\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(items.get(i)).append("\n");
        }

        sb.append("\nитого: ").append(String.format("%.2f", calculateTotalPrice())).append(" руб");
        sb.append("\n------------------------------------\n");

        return sb.toString();
    }
}