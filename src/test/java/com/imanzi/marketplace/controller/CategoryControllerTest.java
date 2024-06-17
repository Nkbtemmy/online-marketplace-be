package com.imanzi.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanzi.marketplace.dto.CategoryDTO;
import com.imanzi.marketplace.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDTO testCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        testCategory = new CategoryDTO();
//        testCategory.setId(1L);
        testCategory.setName("Test Category");
        // Set other properties as needed
    }

    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(testCategory);

        mockMvc.perform(post("/api/v1/marketplace/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory(any(Long.class), any(CategoryDTO.class))).thenReturn(testCategory);

        mockMvc.perform(put("/api/v1/marketplace/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(testCategory);

        mockMvc.perform(get("/api/v1/marketplace/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(testCategory));

        mockMvc.perform(get("/api/v1/marketplace/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Category"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/marketplace/categories/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
