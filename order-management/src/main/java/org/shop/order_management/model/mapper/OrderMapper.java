package org.shop.order_management.model.mapper;

import org.mapstruct.Mapper;
import org.shop.order_management.model.Customer;
import org.shop.order_management.model.Order;
import org.shop.order_management.model.OrderItem;
import org.shop.order_management.model.dto.CustomerDTO;
import org.shop.order_management.model.dto.OrderDTO;
import org.shop.order_management.model.dto.OrderItemDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);

    Order toEntity(OrderDTO orderDTO);

    CustomerDTO toDTO(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);

    OrderItemDTO toDTO(OrderItem orderItem);

    OrderItem toEntity(OrderItemDTO orderItemDTO);

    List<OrderDTO> toDTOs(List<Order> orders);
}
