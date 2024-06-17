package com.imanzi.marketplace.event;

import com.imanzi.marketplace.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String eventType;
    private Order order;
}
