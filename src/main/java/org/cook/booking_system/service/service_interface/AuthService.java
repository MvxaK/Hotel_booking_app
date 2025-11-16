package org.cook.booking_system.service.service_interface;

import org.cook.booking_system.security.auth.AuthResponse;
import org.cook.booking_system.security.auth.LoginRequest;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);

}
