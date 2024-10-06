package org.shop.order_management.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.order_management.model.Customer;
import org.shop.order_management.model.Order;
import org.shop.order_management.model.OrderItem;
import org.shop.order_management.model.dto.CustomerDTO;
import org.shop.order_management.model.dto.OrderDTO;
import org.shop.order_management.model.dto.OrderItemDTO;
import org.shop.order_management.model.enums.OrderStatus;
import org.shop.order_management.model.mapper.OrderMapper;
import org.shop.order_management.repository.CustomerRepository;
import org.shop.order_management.repository.OrderRepository;
import org.shop.order_management.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    private CustomerDTO customerDTO;
    private Customer customer;
    private OrderDTO orderDto;
    private OrderDTO completedOrderDto;
    private Order order;
    private Order completedOrder;
    private OrderDTO expectedOrderDTO;

    @BeforeEach
    void setup() {
        customerDTO = CustomerDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .build();

        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .build();

        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .price(new BigDecimal("12.12"))
                .productName("test")
                .build();

        OrderItem orderItem = OrderItem.builder()
                .price(new BigDecimal("12.12"))
                .productName("test")
                .build();

        orderDto = OrderDTO.builder()
                .customer(customerDTO)
                .totalPrice(BigDecimal.valueOf(100.00))
                .status(OrderStatus.CREATED)
                .orderItems(List.of(orderItemDTO))
                .build();

        order = Order.builder()
                .id(1L)
                .customer(customer)
                .totalPrice(BigDecimal.valueOf(100.00))
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(List.of(orderItem))
                .build();

        completedOrderDto = OrderDTO.builder()
                .customer(customerDTO)
                .totalPrice(BigDecimal.valueOf(100.00))
                .status(OrderStatus.COMPLETED)
                .orderItems(List.of(orderItemDTO))
                .build();

        completedOrder = Order.builder()
                .id(1L)
                .customer(customer)
                .totalPrice(BigDecimal.valueOf(100.00))
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.COMPLETED)
                .orderItems(List.of(orderItem))
                .build();

        expectedOrderDTO = OrderDTO.builder()
                .id(1L)
                .totalPrice(BigDecimal.valueOf(100.00))
                .status(OrderStatus.CREATED)
                .orderItems(List.of(orderItemDTO))
                .build();
    }

    @Test
    void shouldCreateValidOrder() {
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(customerRepository.save(any(Customer.class))).thenReturn(order.getCustomer());
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(expectedOrderDTO);

        OrderDTO result = orderService.createOrder(orderDto);

        assertEquals(expectedOrderDTO, result);
        verify(orderMapper).toEntity(orderDto);
        verify(customerRepository).save(order.getCustomer());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldUpdateOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(any(Order.class))).thenReturn(completedOrderDto);
        when(orderRepository.save(any(Order.class))).thenReturn(completedOrder);

        OrderDTO result = orderService.updateOrder(1L, completedOrderDto);

        assertEquals(completedOrderDto, result);
        assertEquals(completedOrderDto.getStatus(), result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldReturnListOfOrders() {
        List<Order> orders = List.of(order);
        List<OrderDTO> orderDTOs = List.of(orderDto);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDTOs(orders)).thenReturn(orderDTOs);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(orderDTOs, result);
        verify(orderRepository).findAll();
    }
}
