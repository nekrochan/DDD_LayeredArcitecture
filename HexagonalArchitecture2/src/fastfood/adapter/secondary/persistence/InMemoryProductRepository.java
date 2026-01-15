package fastfood.adapter.secondary.persistence;

import fastfood.domain.model.Product;
import fastfood.domain.model.ProductCategory;
import fastfood.domain.port.secondary.ProductRepository;

import java.time.LocalDate;
import java.util.*;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        addProduct(new Product("1", "говяжьи котлеты", 350.0,
                ProductCategory.MEAT, LocalDate.now().plusDays(30)));
        addProduct(new Product("2", "булочки для бургеров", 50.0,
                ProductCategory.BAKERY, LocalDate.now().plusDays(14)));
        addProduct(new Product("3", "сыр чеддер", 200.0,
                ProductCategory.DAIRY, LocalDate.now().plusDays(21)));
        addProduct(new Product("4", "картофель фри", 80.0,
                ProductCategory.VEGETABLES, LocalDate.now().plusDays(20)));
        addProduct(new Product("5", "салат айсберг", 120.0,
                ProductCategory.VEGETABLES, LocalDate.now().plusDays(7)));
        addProduct(new Product("6", "кетчуп", 150.0,
                ProductCategory.SAUCES, LocalDate.now().plusDays(90)));
        addProduct(new Product("7", "майонез", 140.0,
                ProductCategory.SAUCES, LocalDate.now().plusDays(90)));
    }

    private void addProduct(Product product) {
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
    public List<Product> findByCategory(ProductCategory category) {
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getCategory() == category) {
                result.add(product);
            }
        }
        return result;
    }
}