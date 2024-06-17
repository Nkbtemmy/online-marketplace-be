package com.imanzi.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Review;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.service.JwtService;
import com.imanzi.marketplace.service.ProductService;
import com.imanzi.marketplace.service.ReviewService;
import com.imanzi.marketplace.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

//    @Test
//    void testCreateReview() throws Exception {
//        // Mocking request data
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("Authorization", "Bearer mock-token");
//
//        // Mocking user details
//        User mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setEmail("buyer@example.com");
//
//        // Mocking JWT service
//        when(jwtService.extractTokenFromRequest(request)).thenReturn("mock-token");
//        when(jwtService.getUserFromToken(request)).thenReturn(mockUser);
//
//        // Mocking user service
//        when(userService.getUserFromUserDetails(ArgumentMatchers.any())).thenReturn(mockUser);
//
//        // Mocking product service
//        Product mockProduct = new Product();
//        mockProduct.setId(1L);
//        when(productService.getProductById(1L)).thenReturn(mockProduct);
//
//        // Mocking review service
//        Review mockReview = new Review();
//        mockReview.setId(1L);
//        when(reviewService.createReview(ArgumentMatchers.any(Review.class), ArgumentMatchers.any(User.class), ArgumentMatchers.any(Product.class)))
//                .thenReturn(mockReview);
//
//        // Performing POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/marketplace/reviews")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(mockReview))
//                        .param("productId", "1")
//                        .request(request))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
//
//        // Verify interactions
//        verify(jwtService, times(1)).extractTokenFromRequest(request);
//        verify(jwtService, times(1)).getUserFromToken(request);
//        verify(userService, times(1)).getUserFromUserDetails(mockUser);
//        verify(productService, times(1)).getProductById(1L);
//        verify(reviewService, times(1)).createReview(ArgumentMatchers.any(Review.class), ArgumentMatchers.any(User.class), ArgumentMatchers.any(Product.class));
//    }

    @Test
    void testUpdateReview() throws Exception {
        // Mocking review service
        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setReviewText("Updated review content");

        when(reviewService.updateReview(1L, updatedReview)).thenReturn(updatedReview);

        // Performing PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/marketplace/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedReview)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Updated review content"));

        // Verify interactions
        verify(reviewService, times(1)).updateReview(1L, updatedReview);
    }

    @Test
    void testDeleteReview() throws Exception {
        // Performing DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/marketplace/reviews/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify interactions
        verify(reviewService, times(1)).deleteReview(1L);
    }

    @Test
    void testGetReviewById() throws Exception {
        // Mocking review service
        Review mockReview = new Review();
        mockReview.setId(1L);
        when(reviewService.getReviewById(1L)).thenReturn(mockReview);

        // Performing GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marketplace/reviews/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        // Verify interactions
        verify(reviewService, times(1)).getReviewById(1L);
    }

    @Test
    void testGetAllReviews() throws Exception {
        // Mocking review service
        Review mockReview1 = new Review();
        mockReview1.setId(1L);
        Review mockReview2 = new Review();
        mockReview2.setId(2L);
        List<Review> mockReviews = Arrays.asList(mockReview1, mockReview2);
        when(reviewService.getAllReviews()).thenReturn(mockReviews);

        // Performing GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marketplace/reviews"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L));

        // Verify interactions
        verify(reviewService, times(1)).getAllReviews();
    }

    // Helper method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
