package fastfood.domain.model;

public enum OrderStatus {
    CREATED("создан"),
    DRAFT("черновик"),
    SENT_TO_SUPPLIER("отправлен поставщику"),
    IN_TRANSIT("в пути"),
    DELIVERED("доставлен"),
    ACCEPTED("принят (качество ок)"),
    REJECTED("отклонен (проблемы с качеством)"),
    CANCELLED("отменен");

    private String description;

    OrderStatus(String description) {
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