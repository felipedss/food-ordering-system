package com.food.ordering.system.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.event.OrderPaidEvent;
import com.food.ordering.system.service.domain.ports.publisher.DomainEventPublisher;

public interface OrderPaidPaymentRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
