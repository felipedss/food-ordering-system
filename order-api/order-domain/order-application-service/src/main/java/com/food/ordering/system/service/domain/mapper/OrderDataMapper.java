package com.food.ordering.system.service.domain.mapper;

import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.OrderItem;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.dto.create.OrderAddressCommand;
import com.food.ordering.system.service.domain.dto.create.OrderItemCommand;
import com.food.ordering.system.valueobject.StreetAddress;
import com.ordering.system.valueobject.CustomerId;
import com.ordering.system.valueobject.Money;
import com.ordering.system.valueobject.ProductId;
import com.ordering.system.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Order createOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(createStreetAddress(createOrderCommand.getOrderAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(createOrderItems(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse createOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public Restaurant createRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems()
                        .stream()
                        .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }


    private List<OrderItem> createOrderItems(List<OrderItemCommand> items) {
        return items.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subtotal(new Money(orderItem.getSubTotal()))
                        .build()
                ).collect(Collectors.toList());
    }

    private StreetAddress createStreetAddress(OrderAddressCommand orderAddressCommand) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddressCommand.getStreet(),
                orderAddressCommand.getPostalCode(),
                orderAddressCommand.getCity()
        );
    }

}
