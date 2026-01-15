package fastfood.domain.model;

public enum TemperatureMode {
    FROZEN("замороженный"),
    REFRIGERATED("охлажденный"),
    ROOM_TEMPERATURE("комнатная температура");

    private String description;

    TemperatureMode(String description) {
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