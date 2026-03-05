package com.jobhunter.jobhunter_be.exception;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.exception.custom.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleNotFound(NotFoundException exception) {
        log.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                RestResponse.error(
                        HttpStatus.NOT_FOUND.value(),
                        "NOT FOUND",
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestResponse<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        log.warn("Invalid request: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                RestResponse.error(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID REQUEST",
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(message -> message != null && !message.isBlank())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                RestResponse.error(
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION ERROR",
                        detail
                )
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleUsernameNotFound(UsernameNotFoundException exception) {
        log.warn("User not found: {}", exception.getMessage());

        RestResponse<Void> response = RestResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "USER NOT FOUND",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse<Void>> handleBadCredentials(BadCredentialsException exception) {
        log.warn("Bad credentials: {}", exception.getMessage());

        RestResponse<Void> response = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID USERNAME OR PASSWORD",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<RestResponse<Void>> handleExpiredToken(ExpiredJwtException ex) {
        log.warn("Expired JWT: {}", ex.getMessage());
        RestResponse<Void> response = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "TOKEN HAS EXPIRED",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<RestResponse<Void>> handleUnsupportedToken(UnsupportedJwtException ex) {
        log.warn("Unsupported JWT: {}", ex.getMessage());

        RestResponse<Void> response = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "UNSUPPORTED TOKEN",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<RestResponse<Void>> handleMalformedToken(MalformedJwtException ex) {
        log.warn("Malformed JWT: {}", ex.getMessage());

        RestResponse<Void> response = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "MALFORMED TOKEN",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        RestResponse<Void> response = RestResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "ACCESS DENIED",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleRoleNotExistException(RoleNotFoundException e) {
        log.error("Role not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                RestResponse.error(
                        HttpStatus.NOT_FOUND.value(),
                        "ROLE NOT FOUND",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(UsernameExistedException.class)
    public ResponseEntity<RestResponse<Void>> handleUserAlreadyExistedException(UsernameExistedException e) {
        log.warn("Username already existed: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                RestResponse.error(
                        HttpStatus.CONFLICT.value(),
                        "USERNAME ALREADY EXISTED",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<RestResponse<Void>> handleExpiredRefreshToken(ExpiredRefreshTokenException e) {
        log.warn("Expired refreshtoken: {}", e.getMessage());

        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "EXPIRED REFRESHTOKEN",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<RestResponse<Void>> handleInvalidRefreshToken(InvalidRefreshTokenException e) {
        log.warn("Invalid refreshtoken: {}", e);

        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID REFRESHTOKEN: {}",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(InvalidResetPasswordTokenException.class)
    public ResponseEntity<RestResponse<Void>> handleInvalidResetPasswordToken(InvalidResetPasswordTokenException e) {
        log.warn("Invalid resetpassword token: {}", e.getMessage());

        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID RESETPASSWORD TOKEN",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        log.warn("Refresh Token Not Found: {}", e.getMessage());

        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "The provided refresh token does not exist",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(VeryficationTokenNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleVeryficationTokenNotFound(VeryficationTokenNotFoundException e) {
        log.warn("Veryficationtoken not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                RestResponse.error(
                        HttpStatus.NOT_FOUND.value(),
                        "Veryfication not found",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(ExpiredVeryficationToken.class)
    public ResponseEntity<RestResponse<Void>> handleExpiredVeryficationToken(ExpiredVeryficationToken e) {
        log.warn("Expired veryfication token: {}", e.getMessage());

        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "VERIFICATION TOKEN EXPIRED",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        RestResponse<Void> apiResponse = RestResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An error occurred",
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
