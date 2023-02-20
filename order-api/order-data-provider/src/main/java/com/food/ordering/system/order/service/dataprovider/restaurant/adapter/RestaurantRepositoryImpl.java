package com.food.ordering.system.order.service.dataprovider.restaurant.adapter;

import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.order.service.dataprovider.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataprovider.restaurant.mapper.RestaurantDataProviderMapper;
import com.food.ordering.system.order.service.dataprovider.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.service.domain.ports.output.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataProviderMapper restaurantDataProviderMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> uuids = restaurantDataProviderMapper.restaurantToRestaurantProduct(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository.findByRestaurantId(restaurant.getId().getValue(), uuids);
        return restaurantEntities.map(restaurantDataProviderMapper::restaurantEntityToRestaurant);
    }
}
