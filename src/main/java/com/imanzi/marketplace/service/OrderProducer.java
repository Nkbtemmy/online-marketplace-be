package com.imanzi.marketplace.service;

import com.imanzi.marketplace.event.OrderEvent;
import com.imanzi.marketplace.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderEvent(Order order) {
        OrderEvent orderEvent = new OrderEvent("OrderPlaced", order);
        String ordersTopic = "orders";
        kafkaTemplate.send(ordersTopic, orderEvent);
    }
//    public void publish(String topic, String message) {
//        kafkaTemplate.send(topic, message);
//    }
}
