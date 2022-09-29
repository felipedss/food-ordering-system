package com.food.ordering.system.event;

import com.food.ordering.system.entity.Order;
import com.ordering.system.event.DomainEvent;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    protected OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

}
