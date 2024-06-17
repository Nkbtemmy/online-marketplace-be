package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.dto.AuthenticationRequest;
import com.imanzi.marketplace.dto.AuthenticationResponse;
import com.imanzi.marketplace.dto.RegisterRequest;
import com.imanzi.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/marketplace/auths")
@Tag(name = "Authentication", description = "Operations related to authentication management")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthenticationResponse registerUser(@RequestBody RegisterRequest user) {
        return userService.registerUser(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyToken(token);
        if (isVerified) {
            return ResponseEntity.ok("Account verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse loginUser(@RequestBody AuthenticationRequest user) {
        return userService.loginUser(user);
    }

    @GetMapping("/profile")
    public AuthenticationResponse getLoggedInUserProfile(HttpServletRequest request) {
        return userService.getUserProfile(request);
    }

}
