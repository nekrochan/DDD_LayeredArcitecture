package restaurant.infrastructure;

import restaurant.domain.IInventoryRepository;
import restaurant.domain.Inventory;

import java.util.*;

public class InMemoryInventoryRepository implements IInventoryRepository {
    private final Map<String, Inventory> inventories = new HashMap<>();

    @Override
    public void save(Inventory inventory) {
        inventories.put(inventory.getRestaurantName(), inventory);
    }

    @Override
    public Optional<Inventory> findByRestaurantName(String restaurantName) {
        return Optional.ofNullable(inventories.get(restaurantName));
    }

    @Override
    public List<Inventory> findAll() {
        return new ArrayList<>(inventories.values());
    }
}