package com.jobhunter.jobhunter_be.controller;



import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.LoginRequest;
import com.jobhunter.jobhunter_be.dto.request.RegisterRequest;
import com.jobhunter.jobhunter_be.dto.response.AuthResponse;
import com.jobhunter.jobhunter_be.dto.response.UserResponse;
import com.jobhunter.jobhunter_be.exception.custom.*;
import com.jobhunter.jobhunter_be.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "API for login and register")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    @Value("${app.cookie.refresh.secure:true}")
    private boolean refreshCookieSecure;
    @Value("${app.cookie.refresh.same-site:None}")
    private String refreshCookieSameSite;


    @PostMapping("/register")
    @Operation(summary = "Register by email & password", description = "Create a user base on email & password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully create a user"),
            @ApiResponse(responseCode = "409", description = "This email is used for another account")
    })
    public ResponseEntity<RestResponse<Void>> register(
            @Parameter(description = "Register info - email & password", required = true)
            @Valid @RequestBody RegisterRequest request
    ) throws UsernameExistedException, RoleNotFoundException {

        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestResponse.success("User registered successfully")
        );
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login by email and password",
            description = "Authenticate a user using email and password. Returns access and refresh tokens if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<RestResponse<AuthResponse<UserResponse>>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse<UserResponse> authResponse = authService.login(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite(refreshCookieSameSite)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(authResponse, "User logged in successfully")
        );
    }

    @Operation(
            summary = "Logout user",
            description = "Logout by deleting refresh token both in DB and browser cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "404", description = "Refresh token not found in the database")
    })
    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Void>> logout(
            @Parameter(
                    description = "Refresh token stored in the cookie",
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response
    ) throws RefreshTokenNotFoundException {
        authService.logout(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite(refreshCookieSameSite)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok(
                RestResponse.success("Logged out successfully")
        );
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Re-create access token",
            description = "Generate a new access token using the refresh token from the request cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "404", description = "Refresh token not found"),
            @ApiResponse(responseCode = "401", description = "Refresh token has expired")
    })
    public ResponseEntity<RestResponse<AuthResponse<Object>>> refreshToken(
            @Parameter(
                    description = "Refresh token stored in cookie",
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            @CookieValue("refresh_token") String refreshToken
    ) throws RefreshTokenNotFoundException, ExpiredRefreshTokenException {
        AuthResponse<Object> authResponse = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(
                RestResponse.success(authResponse, "Token refreshed successfully")
        );
    }

    @GetMapping("/me")
    private ResponseEntity<RestResponse<UserResponse>> getCurrentUser(Authentication authentication) throws NotFoundException {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.success(
                authService.currentUser(email),
                "Get User successfully"
        ));
    }

}
