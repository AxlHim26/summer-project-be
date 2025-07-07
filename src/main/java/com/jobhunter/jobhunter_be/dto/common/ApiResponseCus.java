package com.jobhunter.jobhunter_be.dto.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseCus<T> {

    private int status;
    private String message;
    private String errorDetail;
    private T data;
    private String path;
    private String timestamp;

    private static String resolveRequestPath() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        }
        return "unknown";
    }

    public static <T> ApiResponseCus<T> success(T data, String message) {
        return ApiResponseCus.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .path(resolveRequestPath())
                .timestamp(Instant.now().toString())
                .build();
    }

    public static <T> ApiResponseCus<T> success(String message) {
        return success(null, message);
    }

    public static <T> ApiResponseCus<T> error(HttpStatus httpStatus, String message, String errorDetail) {
        return ApiResponseCus.<T>builder()
                .status(httpStatus.value())
                .message(message)
                .errorDetail(errorDetail)
                .path(resolveRequestPath())
                .timestamp(Instant.now().toString())
                .build();
    }

    public static <T> ApiResponseCus<T> error(int status, String message, String errorDetail) {
        return ApiResponseCus.<T>builder()
                .status(status)
                .message(message)
                .errorDetail(errorDetail)
                .path(resolveRequestPath())
                .timestamp(Instant.now().toString())
                .build();
    }
}
