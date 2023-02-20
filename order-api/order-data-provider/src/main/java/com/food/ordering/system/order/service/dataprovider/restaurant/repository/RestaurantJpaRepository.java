package com.food.ordering.system.order.service.dataprovider.restaurant.repository;

import com.food.ordering.system.order.service.dataprovider.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataprovider.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    Optional<List<RestaurantEntity>> findByRestaurantId(UUID restaurantId, List<UUID> productIds);

}
