package com.food.ordering.system.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.ports.publisher.DomainEventPublisher;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
