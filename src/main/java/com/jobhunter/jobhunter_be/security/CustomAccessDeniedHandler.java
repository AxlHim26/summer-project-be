package com.jobhunter.jobhunter_be.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobhunter.jobhunter_be.dto.common.ApiResponseCus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.warn("Access denied: {} | Reason: {}", request.getRequestURI(), accessDeniedException.getMessage());

        ApiResponseCus<Object> apiResponse = ApiResponseCus.<Object>builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("You do not have access to this resource.")
                .errorDetail(Optional.ofNullable(accessDeniedException.getCause())
                        .map(Throwable::getMessage)
                        .orElse(accessDeniedException.getMessage()))
                .path(request.getRequestURI())
                .timestamp(Instant.now().toString())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
