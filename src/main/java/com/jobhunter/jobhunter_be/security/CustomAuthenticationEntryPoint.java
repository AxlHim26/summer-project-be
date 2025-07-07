package com.jobhunter.jobhunter_be.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobhunter.jobhunter_be.dto.common.ApiResponseCus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn("Unauthorized access: {} | Reason: {}", request.getRequestURI(), authException.getMessage());

        ApiResponseCus<Object> apiResponse = ApiResponseCus.<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Token is invalid!")
                .errorDetail(Optional.ofNullable(authException.getCause())
                        .map(Throwable::getMessage)
                        .orElse(authException.getMessage()))
                .path(request.getRequestURI())
                .timestamp(Instant.now().toString())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
