package fastfood.adapter.secondary.persistence;
import fastfood.domain.model.Order;
import fastfood.domain.port.secondary.OrderRepository;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> findBySupplierId(String supplierId) {
        List<Order> result = new ArrayList<>();

        for (Order order : orders.values()) {
            if (order.getSupplier().getId().equals(supplierId)) {
                result.add(order);
            }
        }

        result.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return result;
    }
}
