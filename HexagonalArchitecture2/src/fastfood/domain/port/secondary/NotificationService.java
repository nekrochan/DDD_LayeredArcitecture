package fastfood.domain.port.secondary;

import fastfood.domain.model.Order;

public interface NotificationService {
    void notifyOrderStatusChanged(Order order);
    void notifySupplier(String supplierName, String message);
}