package com.food.ordering.system.order.service.dataprovider.restaurant.mapper;

import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.order.service.dataprovider.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataprovider.restaurant.exception.RestaurantDataProviderException;
import com.ordering.system.valueobject.Money;
import com.ordering.system.valueobject.ProductId;
import com.ordering.system.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataProviderMapper {

    public List<UUID> restaurantToRestaurantProduct(Restaurant restaurant) {
        return restaurant.getProducts()
                .stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {

        RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().orElseThrow(() -> new RestaurantDataProviderException("Restaurant could not be found"));

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.isRestaurantActive())
                .build();
    }

}
