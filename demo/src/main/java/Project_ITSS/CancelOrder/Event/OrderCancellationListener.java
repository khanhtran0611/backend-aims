package Project_ITSS.CancelOrder.Event;

import Project_ITSS.CancelOrder.Service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancellationListener {

    @Autowired
    private INotificationService notificationService;

    @EventListener
    public void handleOrderCancellation(OrderCancellationEvent event) {
        notificationService.sendCancellationNotification(event.getOrder(), event.getRefundResult());
    }
} 