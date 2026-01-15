package restaurant.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private String restaurantName;
    private Map<String, Product> products;
    private Map<String, List<Product>> productsByName;

    public Inventory(String restaurantName) {
        this.restaurantName = restaurantName;
        this.products = new HashMap<>();
        this.productsByName = new HashMap<>();
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        productsByName.computeIfAbsent(product.getName().toLowerCase(), k -> new ArrayList<>())
                .add(product);
        cleanZeroQuantityProducts(product.getName());
    }

    public Product findProductById(String productId) {
        return products.get(productId);
    }

    public List<Product> findProductsByName(String productName) {
        return productsByName.getOrDefault(productName.toLowerCase(), new ArrayList<>());
    }

    public void receiveProduct(String productName, double quantity, LocalDate expiryDate) {
        List<Product> sameNameProducts = findProductsByName(productName);

        Optional<Product> sameExpiryProduct = sameNameProducts.stream()
                .filter(p -> p.getExpiryDate().equals(expiryDate))
                .findFirst();


        if (quantity == 0) {
            sameNameProducts.removeIf(p -> p.getExpiryDate().equals(expiryDate));
            updateMaps();
            return;
        }

        if (sameExpiryProduct.isPresent()) {
            Product product = sameExpiryProduct.get();
            product.addQuantity(quantity);
        } else {
            if (sameNameProducts.isEmpty()) {
                Product newProduct = new Product(sameExpiryProduct.orElse(null), expiryDate, quantity);
                addProduct(newProduct);
            } else {
                Product template = sameNameProducts.get(0);
                Product newProduct = new Product(template, expiryDate, quantity);
                addProduct(newProduct);
            }
        }

        cleanZeroQuantityProducts(productName);
    }

    public void useProduct(String productId, double quantity) {
        Product product = findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Продукт не найден в инвентаре");
        }

        if (product.getQuantity() < quantity) {
            List<Product> allSameProducts = findProductsByName(product.getName());

            allSameProducts.sort(Comparator.comparing(Product::getExpiryDate));

            double remainingQuantity = quantity;

            for (Product p : allSameProducts) {
                if (remainingQuantity <= 0) break;

                double available = p.getQuantity();
                if (available > 0) {
                    double toUse = Math.min(available, remainingQuantity);
                    p.useQuantity(toUse);
                    remainingQuantity -= toUse;
                }
            }

            if (remainingQuantity > 0) {
                throw new IllegalStateException("Недостаточно продукта на складе");
            }
        } else {
            product.useQuantity(quantity);
        }

        cleanZeroQuantityProducts(product.getName());
        updateMaps();
    }

    public void writeOffExpiredProduct(String productId) {
        Product product = findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Продукт не найден в инвентаре");
        }
        if (!product.isExpired()) {
            throw new IllegalStateException("Продукт не просрочен");
        }
        product.writeOff();
        cleanZeroQuantityProducts(product.getName());
        updateMaps();
    }

    public void adjustProductQuantity(String productId, double newQuantity) {
        Product product = findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Продукт не найден в инвентаре");
        }
        product.adjustQuantity(newQuantity);
        cleanZeroQuantityProducts(product.getName());
    }

    private void cleanZeroQuantityProducts(String productName) {
        List<Product> productList = findProductsByName(productName);

        long zeroQuantityCount = productList.stream()
                .filter(p -> p.getQuantity() <= 0)
                .count();

        if (zeroQuantityCount > 1) {
            List<Product> zeroProducts = productList.stream()
                    .filter(p -> p.getQuantity() <= 0)
                    .collect(Collectors.toList());

            zeroProducts.sort(Comparator.comparing(Product::getExpiryDate));
            zeroProducts.subList(1, zeroProducts.size()).forEach(p -> {
                products.remove(p.getId());
                productList.remove(p);
            });
        }

        if (productList.isEmpty()) {
            productsByName.remove(productName.toLowerCase());
        } else {
            List<Product> allProducts = new ArrayList<>(products.values());
            for (Product product : allProducts) {
                if (product.getName().equalsIgnoreCase(productName) &&
                        product.getQuantity() <= 0) {

                    List<Product> sameName = findProductsByName(productName);
                    if (sameName.size() > 1 || sameName.size() == 0) {
                        products.remove(product.getId());
                    }
                }
            }
            productsByName.put(productName.toLowerCase(), productList);
        }
    }

    private void updateMaps() {
        List<Product> allProducts = new ArrayList<>(products.values());
        productsByName.clear();

        Map<String, List<Product>> tempMap = new HashMap<>();

        for (Product product : allProducts) {
            String nameKey = product.getName().toLowerCase();
            tempMap.computeIfAbsent(nameKey, k -> new ArrayList<>()).add(product);
        }

        for (Map.Entry<String, List<Product>> entry : tempMap.entrySet()) {
            List<Product> productList = entry.getValue();

            if (productList.isEmpty()) {
                continue;
            }

            long zeroCount = productList.stream()
                    .filter(p -> p.getQuantity() <= 0)
                    .count();

            if (zeroCount > 1) {
                List<Product> zeroProducts = productList.stream()
                        .filter(p -> p.getQuantity() <= 0)
                        .sorted(Comparator.comparing(Product::getExpiryDate))
                        .collect(Collectors.toList());

                for (int i = 1; i < zeroProducts.size(); i++) {
                    Product zeroProduct = zeroProducts.get(i);
                    products.remove(zeroProduct.getId());
                    productList.remove(zeroProduct);
                }
            }

            if (!productList.isEmpty()) {
                productsByName.put(entry.getKey(), productList);
            }
        }
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> getExpiredProducts() {
        return products.values().stream()
                .filter(Product::isExpired)
                .collect(Collectors.toList());
    }

    public List<Product> getCriticalLevelProducts() {
        return products.values().stream()
                .filter(Product::isBelowCriticalLevel)
                .collect(Collectors.toList());
    }

    public List<Product> getBelowMinLevelProducts() {
        return products.values().stream()
                .filter(Product::isBelowMinLevel)
                .collect(Collectors.toList());
    }

    public double getTotalInventoryValue() {
        return products.values().stream()
                .mapToDouble(Product::getQuantity)
                .sum();
    }
}