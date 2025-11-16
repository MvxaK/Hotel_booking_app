package org.cook.booking_system.controller.authController;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.security.auth.AuthResponse;
import org.cook.booking_system.security.auth.LoginRequest;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.cook.booking_system.service.implementation.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthResponse authResponse = authService.register(registerRequest);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.login(loginRequest);

        return ResponseEntity.ok(authResponse);
    }

}
