package com.imanzi.marketplace.service;

import com.imanzi.marketplace.model.Order;
import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.model.enums.OrderStatus;
import com.imanzi.marketplace.repository.OrderRepository;
import com.imanzi.marketplace.repository.ProductRepository;
import com.imanzi.marketplace.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final ProductRepository productRepository;
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final OrderProducer orderProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository, JwtService jwtService, ProductRepository productRepository, HttpServletRequest request, UserRepository userRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.productRepository = productRepository;
        this.request = request;
        this.userRepository = userRepository;
        this.orderProducer = orderProducer;
    }

    public Order updateOrderStatus(Long id, String status) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        existingOrder.setStatus(OrderStatus.valueOf(status));
        return orderRepository.save(existingOrder);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order placeOrder(List<Long> productIds) {
        User user = (User) jwtService.getUserFromToken(request);

        List<Product> products = productRepository.findAllById(productIds);

        Order order = Order.builder()
                .user(user)
                .products(products)
                .status(OrderStatus.PENDING)
                .orderDate(LocalDate.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        orderProducer.sendOrderEvent(savedOrder);
        return savedOrder;
    }

    public List<Order> getOrdersForCurrentUser() {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
