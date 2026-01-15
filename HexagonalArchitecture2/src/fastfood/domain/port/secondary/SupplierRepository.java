package fastfood.domain.port.secondary;

import fastfood.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    void save(Supplier supplier);
    Optional<Supplier> findById(String id);
    List<Supplier> findAll();
}