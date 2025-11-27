package org.cook.booking_system.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final Throwable cause = (Throwable) request.getAttribute("exception");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        int status = HttpStatus.UNAUTHORIZED.value();
        String message = "Unauthorized";

        if (cause instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED.value();
            message = "JWT Token has expired. Please log in again.";
        } else {
            message = authException.getMessage();
        }

        response.setStatus(status);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("error", "Unauthorized");
        body.put("message", message);
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}