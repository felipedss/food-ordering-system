package com.food.ordering.system.order.service.dataprovider.order.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_items")
@Entity
@IdClass(OrderItemEntityId.class)
@EqualsAndHashCode(of = "id")
public class OrderItemEntity {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private UUID productId;

    private BigDecimal price;

    private int quantity;

    private BigDecimal subtTotal;
}
