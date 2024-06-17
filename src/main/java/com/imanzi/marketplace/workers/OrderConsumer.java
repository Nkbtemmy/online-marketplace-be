package com.imanzi.marketplace.workers;

import com.imanzi.marketplace.event.OrderEvent;
import com.imanzi.marketplace.model.Order;
import com.imanzi.marketplace.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consumeOrderEvent(OrderEvent orderEvent) {
//        System.out.println("Received order event: " + orderEvent);
        Order order = orderEvent.getOrder();
        orderRepository.save(order);
    }
}
