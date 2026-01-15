package fastfood.adapter.secondary.notification;

import fastfood.domain.model.Order;
import fastfood.domain.port.secondary.NotificationService;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyOrderStatusChanged(Order order) {
        System.out.println("\n[уведомление поставщику] статус заказа поставщику №" +
                order.getId() + " изменен на: " + order.getStatus());
    }

    @Override
    public void notifySupplier(String supplierName, String message) {
        System.out.println("\n[уведомление] поставщику " + supplierName + ": " + message);
    }
}