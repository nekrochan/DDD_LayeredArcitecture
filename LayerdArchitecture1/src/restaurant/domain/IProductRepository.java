package restaurant.domain;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    void save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    List<Product> findExpiredProducts();
    List<Product> findCriticalLevelProducts();
    List<Product> findByCategory(String category);
}