package com.food.ordering.system.order.service.dataprovider.customer.adapter;

import com.food.ordering.system.entity.Customer;
import com.food.ordering.system.order.service.dataprovider.customer.mapper.CustomerDataProviderMapper;
import com.food.ordering.system.order.service.dataprovider.customer.repository.CustomerJpaRepository;
import com.food.ordering.system.service.domain.ports.output.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataProviderMapper customerDataProviderMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerDataProviderMapper::customerEntityToCustomer);
    }
}
