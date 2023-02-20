package com.food.ordering.system.order.service.dataprovider.order.adapter;

import com.food.ordering.system.entity.Order;
import com.food.ordering.system.order.service.dataprovider.order.mapper.OrderDataProviderMapper;
import com.food.ordering.system.order.service.dataprovider.order.repository.OrderJpaRepository;
import com.food.ordering.system.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.valueobject.TrackingId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataProviderMapper orderDataProviderMapper;
    @Override
    public Order save(Order order) {
        return orderDataProviderMapper.orderEntityToOrder(orderJpaRepository.save(orderDataProviderMapper.OrderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataProviderMapper::orderEntityToOrder);
    }
}
