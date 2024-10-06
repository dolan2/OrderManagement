package org.shop.order_management.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.shop.order_management.model.Order;
import org.shop.order_management.model.enums.OrderStatus;
import org.shop.order_management.model.dto.OrderDTO;
import org.shop.order_management.model.mapper.OrderMapper;
import org.shop.order_management.repository.CustomerRepository;
import org.shop.order_management.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        order.setCustomer(customerRepository.save(order.getCustomer()));
        order.getOrderItems().forEach(item -> item.setOrder(order));

        return orderMapper.toDTO(orderRepository.save(order));
    }

    public OrderDTO updateOrder(Long orderId, OrderDTO orderDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> existingOrder.toBuilder()
                        .status(orderDto.getStatus())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .map(updatedOrder -> orderMapper.toDTO(orderRepository.save(updatedOrder)))
                .orElseThrow(() -> new EntityNotFoundException(orderId.toString()));
    }

    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }

}
