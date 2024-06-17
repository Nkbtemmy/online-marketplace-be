package com.imanzi.marketplace.service;

import com.imanzi.marketplace.dto.AuthenticationRequest;
import com.imanzi.marketplace.dto.AuthenticationResponse;
import com.imanzi.marketplace.dto.RegisterRequest;
import org.springframework.util.StringUtils;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.model.enums.Role;
import com.imanzi.marketplace.repository.UserRepository;
import com.imanzi.marketplace.util.EmailService;
import com.imanzi.marketplace.util.exception.UserException;
import com.imanzi.marketplace.util.template.AuthenticationTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${web.url}")
    private String webURL;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public AuthenticationResponse registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserException("EMAIL_IN_USE", "Email already in use");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.BUYER)
                .emailVerified(false)
                .build();

        userRepository.save(user);

        // Generate verification token
        var jwtToken = jwtService.generateToken(user);
        String verificationLink = webURL + "/api/auth/verify?token=" + jwtToken;

        String message = AuthenticationTemplate.generateAccountVerificationHtml(user, verificationLink);
        boolean emailStatus = emailService.sendEmail(user.getEmail(), "Email verification", message);

        if (!emailStatus) {
            userRepository.delete(user);
            throw new UserException("EMAIL_SENDING_FAILED", "Failed to send verification email");
        }

        return AuthenticationResponse.builder()
                .message("You have successfully registered, please check your email address for confirmation")
                .build();
    }

    public AuthenticationResponse loginUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserException("USER_NOT_FOUND", "User not found"));

        // Check if the user is verified
        if (!user.isEmailVerified()) {
            throw new UserException("EMAIL_NOT_VERIFIED", "Email not verified");
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .data(user)
                .build();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserFromUserDetails(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (!user.isEmailVerified()) {
            throw new UserException("EMAIL_NOT_VERIFIED", "Email is not verified");
        }
        return user;
    }

    public boolean verifyToken(String token) {
        String userEmail = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserException("USER_NOT_FOUND", "User not found"));
        if (user != null && jwtService.isTokenValid(token, user)) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public AuthenticationResponse getUserProfile(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
        if (!StringUtils.hasText(token)) {
            throw new UserException("TOKEN_MISSING", "Token is missing or invalid");
        }

        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserException("USER_NOT_FOUND", "User not found"));
        if (!user.isEmailVerified()) {
            throw new UserException("EMAIL_NOT_VERIFIED", "Email is not verified");
        }
        return AuthenticationResponse.builder()
                .data(user)
                .build();
    }

}
