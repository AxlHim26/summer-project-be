package com.jobhunter.jobhunter_be.exception;

import com.jobhunter.jobhunter_be.dto.common.ApiResponseCus;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleUsernameNotFound(UsernameNotFoundException exception) {
        log.warn("User not found: {}", exception.getMessage());

        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.NOT_FOUND.value(),
                "USER NOT FOUND",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleBadCredentials(BadCredentialsException exception) {
        log.warn("Bad credentials: {}", exception.getMessage());

        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID USERNAME OR PASSWORD",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleExpiredToken(ExpiredJwtException ex) {
        log.warn("Expired JWT: {}", ex.getMessage());
        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "TOKEN HAS EXPIRED",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleUnsupportedToken(UnsupportedJwtException ex) {
        log.warn("Unsupported JWT: {}", ex.getMessage());

        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "UNSUPPORTED TOKEN",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleMalformedToken(MalformedJwtException ex) {
        log.warn("Malformed JWT: {}", ex.getMessage());

        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "MALFORMED TOKEN",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        ApiResponseCus<Void> response = ApiResponseCus.error(
                HttpStatus.FORBIDDEN.value(),
                "ACCESS DENIED",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleRoleNotExistException(RoleNotFoundException e) {
        log.error("Role not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponseCus.error(
                        HttpStatus.NOT_FOUND.value(),
                        "ROLE NOT FOUND",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(UsernameExistedException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleUserAlreadyExistedException(UsernameExistedException e) {
        log.warn("Username already existed: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseCus.error(
                        HttpStatus.CONFLICT.value(),
                        "USERNAME ALREADY EXISTED",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleExpiredRefreshToken(ExpiredRefreshTokenException e) {
        log.warn("Expired refreshtoken: {}", e.getMessage());

        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "EXPIRED REFRESHTOKEN",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleInvalidRefreshToken(InvalidRefreshTokenException e) {
        log.warn("Invalid refreshtoken: {}", e);

        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID REFRESHTOKEN: {}",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(InvalidResetPasswordTokenException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleInvalidResetPasswordToken(InvalidResetPasswordTokenException e) {
        log.warn("Invalid resetpassword token: {}", e.getMessage());

        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID RESETPASSWORD TOKEN",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        log.warn("Refresh Token Not Found: {}", e.getMessage());

        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.NOT_FOUND.value(),
                "The provided refresh token does not exist",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(VeryficationTokenNotFoundException.class)
    public ResponseEntity<ApiResponseCus<Void>> handleVeryficationTokenNotFound(VeryficationTokenNotFoundException e) {
        log.warn("Veryficationtoken not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponseCus.error(
                        HttpStatus.NOT_FOUND.value(),
                        "Veryfication not found",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(ExpiredVeryficationToken.class)
    public ResponseEntity<ApiResponseCus<Void>> handleExpiredVeryficationToken(ExpiredRefreshTokenException e) {
        log.warn("Expired veryfication token: {}", e.getMessage());

        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.BAD_REQUEST.value(),
                "Veryfication not found",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseCus<Void>> handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ApiResponseCus<Void> apiResponse = ApiResponseCus.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An error occurred",
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
