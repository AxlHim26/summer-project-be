package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.LoginRequest;
import com.jobhunter.jobhunter_be.dto.request.RegisterRequest;
import com.jobhunter.jobhunter_be.dto.response.AuthResponse;
import com.jobhunter.jobhunter_be.entity.RefreshToken;
import com.jobhunter.jobhunter_be.entity.Role;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.*;
import com.jobhunter.jobhunter_be.repository.RefreshTokenRepository;
import com.jobhunter.jobhunter_be.repository.RoleRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.security.CustomUserDetails;
import com.jobhunter.jobhunter_be.security.JwtService;
import com.jobhunter.jobhunter_be.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void register(RegisterRequest request) throws UsernameExistedException, RoleNotFoundException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameExistedException("Email is already in use");
        }

        Role role = roleRepository.findByName("ROLE_" + request.getRole().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        User user = User.builder()
                .name(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
    }

    @Transactional
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.user();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        try {
            RefreshToken tokenEntity = refreshTokenRepository.findByUser(user)
                    .orElse(RefreshToken.builder().user(user).build());

            tokenEntity.setToken(refreshToken);
            tokenEntity.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

            refreshTokenRepository.save(tokenEntity);
        } catch (Exception e) {
            log.error("Failed to save refresh token for user {}", user.getEmail(), e);
            throw new RuntimeException("Failed to issue refresh token. Please try again later.");
        }

        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getName())
                .role(user.getRole())
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) throws RefreshTokenNotFoundException {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        refreshTokenRepository.delete(token);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) throws RefreshTokenNotFoundException, ExpiredRefreshTokenException {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new ExpiredRefreshTokenException("Refresh token expired");
        }

        User user = token.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getName())
                .role(user.getRole())
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
