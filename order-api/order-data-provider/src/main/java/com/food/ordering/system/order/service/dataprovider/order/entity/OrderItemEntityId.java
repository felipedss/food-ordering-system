package com.food.ordering.system.order.service.dataprovider.order.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "order"})
public class OrderItemEntityId implements Serializable {

    private Long id;
    private OrderEntity order;

}
