package org.cook.booking_system;

import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.security.auth.AuthResponse;
import org.cook.booking_system.security.auth.LoginRequest;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.cook.booking_system.service.implementation.AuthServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserRepository userRepository;

    private static final String userName = "Immortal_NoName";
    private static final String email = "immortal_noname@gmail.com";
    private static final String password = "noname";

    @Test
    @Order(1)
    void register() {
        RegisterRequest request = new RegisterRequest();
        request.setUserName(userName);
        request.setEmail(email);
        request.setPassword(password);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(userName, response.getUserName());
        assertEquals(email, response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
        assertEquals("Bearer", response.getType());

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    @Order(2)
    void login() {
        LoginRequest request = new LoginRequest();
        request.setUserName(userName);
        request.setPassword(password);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(userName, response.getUserName());
        assertEquals("ROLE_USER", response.getRole());

        request.setPassword("invalid_password");

        assertThrows(AuthenticationException.class, () -> {
            authService.login(request);
        });

        userRepository.findByUserName(userName).ifPresent(user -> {
            userRepository.delete(user);
        });
    }

}