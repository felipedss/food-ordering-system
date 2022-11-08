package com.food.ordering.system.service.domain;

import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.ports.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements ApplicationEventPublisherAware, DomainEventPublisher<OrderCreatedEvent> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("Order created event is published for order id {}", domainEvent.getOrder().getId().getValue());
    }
}
