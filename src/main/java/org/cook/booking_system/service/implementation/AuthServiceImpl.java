package org.cook.booking_system.service.implementation;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.entity.RoleEntity;
import org.cook.booking_system.entity.UserEntity;
import org.cook.booking_system.model.Role;
import org.cook.booking_system.repository.RoleRepository;
import org.cook.booking_system.repository.UserRepository;
import org.cook.booking_system.security.auth.AuthResponse;
import org.cook.booking_system.security.auth.LoginRequest;
import org.cook.booking_system.security.auth.RegisterRequest;
import org.cook.booking_system.security.jwt.JwtService;
import org.cook.booking_system.service.service_interface.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        UserEntity userEntity = userRepository.findByUserName(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new AuthResponse(jwt, "Bearer", userDetails.getUsername(), userEntity.getEmail(), userEntity.getId());
    }


    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByUserName(registerRequest.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(registerRequest.getUsername());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByRole(Role.ROLE_USER);
        userEntity.setRoles(Set.of(userRole));

        UserEntity savedUser = userRepository.save(userEntity);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt, "Bearer", userDetails.getUsername(), savedUser.getEmail(), savedUser.getId());
    }
}
