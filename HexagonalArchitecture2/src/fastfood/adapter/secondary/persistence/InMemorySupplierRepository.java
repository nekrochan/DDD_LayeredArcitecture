package fastfood.adapter.secondary.persistence;

import fastfood.domain.model.Supplier;
import fastfood.domain.port.secondary.SupplierRepository;

import java.util.*;

public class InMemorySupplierRepository implements SupplierRepository {
    private final Map<String, Supplier> suppliers = new HashMap<>();

    public InMemorySupplierRepository() {
        addSupplier(new Supplier("1", "мясная компания 'Мясной мир'",
                "+7-495-111-11-11", "premier@meat.ru"));
        addSupplier(new Supplier("2", "овощной комплекс 'Овощи-фрукты'",
                "+7-495-222-22-22", "fresh@veg.ru"));
        addSupplier(new Supplier("3", "молочный завод 'Молочная река кисельные берега'",
                "+7-495-333-33-33", "info@lactose.ru"));
        addSupplier(new Supplier("4", "хлебозавод 'Мир булочек'",
                "+7-495-444-44-44", "order@bulochka.ru"));
        addSupplier(new Supplier("5", "соусная фабрика 'Мир соусов'",
                "+7-495-555-55-55", "sales@smak.ru"));
    }

    private void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getId(), supplier);
    }

    @Override
    public Optional<Supplier> findById(String id) {
        return Optional.ofNullable(suppliers.get(id));
    }

    @Override
    public List<Supplier> findAll() {
        return new ArrayList<>(suppliers.values());
    }

    @Override
    public void save(Supplier supplier) {
        suppliers.put(supplier.getId(), supplier);
    }
}