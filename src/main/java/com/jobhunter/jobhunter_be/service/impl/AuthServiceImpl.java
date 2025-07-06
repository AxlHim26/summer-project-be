package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.LoginRequest;
import com.jobhunter.jobhunter_be.dto.request.RegisterRequest;
import com.jobhunter.jobhunter_be.dto.response.AuthResponse;
import com.jobhunter.jobhunter_be.entity.RefreshToken;
import com.jobhunter.jobhunter_be.entity.Role;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.ExpiredRefreshTokenException;
import com.jobhunter.jobhunter_be.exception.custom.RefreshTokenNotFoundException;
import com.jobhunter.jobhunter_be.exception.custom.RoleNotFoundException;
import com.jobhunter.jobhunter_be.exception.custom.UsernameExistedException;
import com.jobhunter.jobhunter_be.repository.RefreshTokenRepository;
import com.jobhunter.jobhunter_be.repository.RoleRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.security.JwtService;
import com.jobhunter.jobhunter_be.security.CustomUserDetails;
import com.jobhunter.jobhunter_be.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
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
            throw new UsernameExistedException("Email already in use");
        }

        Role role = roleRepository.findByName("ROLE_" + request.getRole().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        User user = User.builder()
                .name(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
    }

    @Transactional
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.user();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenRepository.deleteByUser(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(tokenEntity);

        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getName())
                .roles(user.getRoles())
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
                .roles(user.getRoles())
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
