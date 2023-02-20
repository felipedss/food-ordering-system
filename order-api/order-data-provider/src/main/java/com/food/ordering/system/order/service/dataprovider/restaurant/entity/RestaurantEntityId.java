package com.food.ordering.system.order.service.dataprovider.restaurant.entity;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"restaurantId", "productId"})
public class RestaurantEntityId implements Serializable {

    @Id
    private UUID restaurantId;

    @Id
    private UUID productId;

}
