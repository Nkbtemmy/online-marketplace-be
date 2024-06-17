package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.dto.AuthenticationRequest;
import com.imanzi.marketplace.dto.AuthenticationResponse;
import com.imanzi.marketplace.dto.RegisterRequest;
import com.imanzi.marketplace.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("testuser@example.com")
                .password("password")
                .build();

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser");
        authenticationRequest.setPassword("password");

        authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("dummy-token");
    }

    @Test
    void testRegisterUser() throws Exception {
        Mockito.when(userService.registerUser(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/marketplace/auths/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\": \"John\", \"lastname\": \"Doe\", \"email\": \"testuser@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"))
                .andDo(print());
    }

    @Test
    void testVerifyAccount() throws Exception {
        Mockito.when(userService.verifyToken("valid-token")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marketplace/auths/verify")
                        .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully."))
                .andDo(print());
    }

    @Test
    void testVerifyAccountWithInvalidToken() throws Exception {
        Mockito.when(userService.verifyToken("invalid-token")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marketplace/auths/verify")
                        .param("token", "invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired token."))
                .andDo(print());
    }

    @Test
    void testLoginUser() throws Exception {
        Mockito.when(userService.loginUser(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/marketplace/auths/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"))
                .andDo(print());
    }

    @Test
    void testGetLoggedInUserProfile() throws Exception {
        Mockito.when(userService.getUserProfile(any(HttpServletRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marketplace/auths/profile")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"))
                .andDo(print());
    }
}
