package com.food.ordering.system.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.event.OrderCancelledEvent;
import com.food.ordering.system.service.domain.ports.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
