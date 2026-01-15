package fastfood.domain.port.secondary;

import fastfood.domain.model.Product;
import fastfood.domain.model.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    List<Product> findAll();
    List<Product> findByCategory(ProductCategory category);
}