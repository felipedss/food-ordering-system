package com.food.ordering.system.entity;

import com.ordering.system.entity.AggregateRoot;
import com.ordering.system.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
