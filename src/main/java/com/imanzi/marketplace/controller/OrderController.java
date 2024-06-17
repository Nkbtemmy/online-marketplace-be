package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.model.Order;
import com.imanzi.marketplace.repository.OrderRepository;
import com.imanzi.marketplace.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace/orders")
@Tag(name = "Order Management", description = "Operations related to product ordering management")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BUYER')")
    public ResponseEntity<Order> placeOrder( @RequestParam List<Long> productIds) {
        Order order = orderService.placeOrder( productIds);
        return ResponseEntity.ok(order);
    }


    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BUYER')")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<List<Order>> getMyOrders() {
        List<Order> orders = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(orders);
    }


}

