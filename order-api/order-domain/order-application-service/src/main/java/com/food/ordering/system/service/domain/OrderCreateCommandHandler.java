package com.food.ordering.system.service.domain;

import com.food.ordering.system.OrderDomainService;
import com.food.ordering.system.entity.Customer;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.exception.OrderDomainException;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     CustomerRepository customerRepository,
                                     OrderRepository orderRepository,
                                     RestaurantRepository restaurantRepository, OrderDataMapper orderDataMapper,
                                     ApplicationDomainEventPublisher applicationDomainEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
        this.applicationDomainEventPublisher = applicationDomainEventPublisher;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = getRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitializeOrder(order, restaurant);
        Order orderSaved = save(order);
        applicationDomainEventPublisher.publish(orderCreatedEvent);
        log.info("Order is created with id: {}", orderSaved.getId().getValue());
        return orderDataMapper.createOrderResponse(order);
    }

    private Restaurant getRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant =  orderDataMapper.createRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: "+ createOrderCommand.getRestaurantId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customer id {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id "+ customerId);
        }
    }

    private Order save(Order order) {
        return orderRepository.save(order);
    }

}
