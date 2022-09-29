package com.food.ordering.system.entity;

import com.food.ordering.system.exception.OrderDomainException;
import com.food.ordering.system.valueobject.OrderItemId;
import com.food.ordering.system.valueobject.StreetAddress;
import com.food.ordering.system.valueobject.TrackingId;
import com.ordering.system.entity.AggregateRoot;
import com.ordering.system.valueobject.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ordering.system.valueobject.OrderStatus.*;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;

    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;

    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = PENDING;
        initializeOrderItems();
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != PAID) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = CANCELLING;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).collect(Collectors.toList()));
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public void cancel(List<String> failureMessages) {
        if (orderStatus != PENDING && orderStatus != CANCELLING) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = CANCELLED;
        updateFailureMessages(failureMessages);
    }

    public void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        orderStatus = PAID;
    }

    public void approve() {
        if (orderStatus != PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation");
        }
        orderStatus = APPROVED;
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubtotal();
                }).reduce(Money.ZERO, Money::add);
        if (price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                + "is not equal to Order items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount()
                    + " is not valid for product " + orderItem.getProduct().getName());
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Order price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null && getId() != null) {
            throw new OrderDomainException("Order is not in the correct state!");
        }
    }

    private Order(Builder builder) {
        this.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
