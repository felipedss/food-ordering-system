package com.food.ordering.system.service.domain.ports.publisher;

import com.ordering.system.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
