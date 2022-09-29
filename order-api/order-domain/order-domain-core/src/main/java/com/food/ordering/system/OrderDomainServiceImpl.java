package com.food.ordering.system;

import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.event.OrderCancelledEvent;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.event.OrderPaidEvent;
import com.food.ordering.system.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitializeOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        return null;
    }

    @Override
    public void approveOrder(Order order) {

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        return null;
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {

    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is not active!");
        }
    }

}
