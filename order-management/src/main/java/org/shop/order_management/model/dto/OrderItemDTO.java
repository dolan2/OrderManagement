package org.shop.order_management.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderItemDTO {
    private Long id;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
