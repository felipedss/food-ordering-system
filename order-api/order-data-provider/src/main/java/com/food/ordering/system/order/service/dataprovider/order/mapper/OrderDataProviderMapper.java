package com.food.ordering.system.order.service.dataprovider.order.mapper;

import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.OrderItem;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.order.service.dataprovider.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataprovider.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataprovider.order.entity.OrderItemEntity;
import com.food.ordering.system.valueobject.OrderItemId;
import com.food.ordering.system.valueobject.StreetAddress;
import com.food.ordering.system.valueobject.TrackingId;
import com.ordering.system.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataProviderMapper {

    public OrderEntity OrderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity
                .builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .orderAddressEntity(orderAddressToOrderAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemsEntity(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .build();
        orderEntity.getOrderAddressEntity().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getOrderAddressEntity()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemsEntityToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(List.of("Item 1, Item 2"))
                .build();
    }

    private List<OrderItem> orderItemsEntityToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductId(orderItemEntity.getProductId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subtotal(new Money(orderItemEntity.getSubtTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity orderAddressEntity) {
        return new StreetAddress(orderAddressEntity.getId(), orderAddressEntity.getStreet(), orderAddressEntity.getPostalCode(), orderAddressEntity.getCity());
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntity(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subtTotal(orderItem.getSubtotal().getAmount())
                        .build())
                .collect(Collectors.toList());
    }

    private OrderAddressEntity orderAddressToOrderAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.id())
                .postalCode(deliveryAddress.postalCode())
                .city(deliveryAddress.city())
                .build();
    }

}
