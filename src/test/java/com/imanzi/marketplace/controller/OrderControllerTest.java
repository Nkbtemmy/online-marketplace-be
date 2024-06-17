package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.model.Order;
import com.imanzi.marketplace.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPlaceOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/marketplace/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]")) // Example JSON content for product IDs
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // Assuming OrderDTO has an 'id' field
    }
}
