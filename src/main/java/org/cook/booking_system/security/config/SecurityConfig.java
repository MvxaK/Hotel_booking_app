package org.cook.booking_system.security.config;

import lombok.RequiredArgsConstructor;
import org.cook.booking_system.security.handler.CustomAccessDeniedHandler;
import org.cook.booking_system.security.jwt.JwtAuthenticationEntryPoint;
import org.cook.booking_system.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers("/rooms", "/houses", "/hotels").permitAll()
                        .requestMatchers("/hotels/**", "/rooms/**", "/houses/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/bookings/**").authenticated()
                        .requestMatchers("/users/my-profile").authenticated()
                        .requestMatchers("/hotels/new", "/houses/new", "/rooms/new", "/room-types/new").hasRole("ADMIN")
                        .requestMatchers("/hotels/*/edit", "/rooms/*/edit", "/houses/*/edit", "/room-types/**").hasRole("ADMIN")
                        .requestMatchers("/users/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }
}