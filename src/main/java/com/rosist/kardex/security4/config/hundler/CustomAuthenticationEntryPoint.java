package com.rosist.kardex.security4.config.hundler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rosist.kardex.dto.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        accessDeniedHandler.handle(request, response, new AccessDeniedException("Access Denied."));
//        ApiError apiError = new ApiError();
//        apiError.setBackendMessage(authException.getLocalizedMessage());
//        apiError.setUrl(request.getRequestURL().toString());
//        apiError.setMethod(request.getMethod());
//        apiError.setMessage("No se encontraron credenciales de authenticacion."
//                + " Por favor inicie session para acceder a esta funcion");
//        apiError.setTimestamp(LocalDateTime.now() );
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);      // "application/json"
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        String apiErrorAsJson = objectMapper.writeValueAsString(apiError);
//        response.getWriter().write(apiErrorAsJson);
    }
}
