package restaurant.domain;

import java.util.List;
import java.util.Optional;

public interface IInventoryRepository {
    void save(Inventory inventory);
    Optional<Inventory> findByRestaurantName(String restaurantName);
    List<Inventory> findAll();
}