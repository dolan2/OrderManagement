package org.shop.order_management.model.dto;

import lombok.Builder;
import lombok.Data;
import org.shop.order_management.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private CustomerDTO customer;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private List<OrderItemDTO> orderItems;
}

