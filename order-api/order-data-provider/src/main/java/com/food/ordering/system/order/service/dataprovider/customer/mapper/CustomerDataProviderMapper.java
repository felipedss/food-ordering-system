package com.food.ordering.system.order.service.dataprovider.customer.mapper;

import com.food.ordering.system.entity.Customer;
import com.food.ordering.system.order.service.dataprovider.customer.entity.CustomerEntity;
import com.ordering.system.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataProviderMapper {

    public Customer customerEntityToCustomer(CustomerEntity customer) {
        return new Customer(new CustomerId(customer.getId()));
    }


}
