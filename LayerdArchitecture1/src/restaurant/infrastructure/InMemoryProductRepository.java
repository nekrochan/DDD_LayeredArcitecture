package restaurant.infrastructure;

import restaurant.domain.IProductRepository;
import restaurant.domain.Product;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements IProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> findExpiredProducts() {
        return products.values().stream()
                .filter(Product::isExpired)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findCriticalLevelProducts() {
        return products.values().stream()
                .filter(Product::isBelowCriticalLevel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Product> findByName(String name) {
        return products.values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}