package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.entity.*;
import com.food.ordering.system.exception.OrderDomainException;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.dto.create.OrderAddressCommand;
import com.food.ordering.system.service.domain.dto.create.OrderItemCommand;
import com.food.ordering.system.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.ports.output.repository.RestaurantRepository;
import com.ordering.system.valueobject.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderDomainServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @InjectMocks
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;

    private CreateOrderCommand createOrderCommandWrongPrice;

    private CreateOrderCommand getCreateOrderCommandWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID RESTAURANT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID PRODUCT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID ORDER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .price(PRICE)
                .orderAddress(OrderAddressCommand
                        .builder()
                        .street("street_1")
                        .postalCode("88802020")
                        .city("Paris")
                        .build())
                .items(List.of(OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .price(new BigDecimal("250.00"))
                .orderAddress(OrderAddressCommand
                        .builder()
                        .street("street_1")
                        .postalCode("88802020")
                        .city("Paris")
                        .build())
                .items(List.of(OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        getCreateOrderCommandWrongProductPrice = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .price(new BigDecimal("250.00"))
                .orderAddress(OrderAddressCommand
                        .builder()
                        .street("street_1")
                        .postalCode("88802020")
                        .city("Paris")
                        .build())
                .items(List.of(OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItemCommand.
                                builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),
                                "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),
                                "product-2",
                                new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();


        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(any())).thenReturn(Optional.of(restaurant));
        Order order = orderDataMapper.createOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));
        when(orderRepository.save(any())).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertEquals(OrderStatus.PENDING, orderResponse.getOrderStatus());
        Assertions.assertNotNull(orderResponse.getOrderTrackingId());
    }

    @Test
    public void testCreateOrderWithWrongPrice() {
        OrderDomainException orderDomainException = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        Assertions.assertEquals(orderDomainException.getMessage(),
                "Total price: 250.00 is not equal to Order items total: 200.00!");
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException orderDomainException = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(getCreateOrderCommandWrongProductPrice));
        Assertions.assertEquals(orderDomainException.getMessage(),
                "Order item price: 60.00 is not valid for product " + PRODUCT_ID);
    }

}
