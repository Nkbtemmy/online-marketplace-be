package com.imanzi.marketplace.util.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSubscriberUtil {

    @KafkaListener(topics = "orders")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        String message = consumerRecord.value();
        System.out.println("--------------------Payment----------"+ message);
        // handle message
    }

}
