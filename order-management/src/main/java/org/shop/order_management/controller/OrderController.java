package org.shop.order_management.controller;

import lombok.AllArgsConstructor;
import org.shop.order_management.model.dto.OrderDTO;
import org.shop.order_management.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderManagement/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        //TODO Biznesowe:
        //[FRONTEND] Klikalne wiersze na tabeli przenoszące nas po routingu w szczegóły zamówienia i tam szczegółowy podgląd wraz z edycją (osobne DTO)
        //[BACKEND] Osobne API dla customera, tak żeby można było reużyć istniejącego klienta

        //TODO Techniczne:
        //[BACKEND] Paginacja i sortowanie na backendzie
        //[BACKEND] Dodać testcontainers + uspójnić inicjalizacje testowych danych
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, order));
    }
}
