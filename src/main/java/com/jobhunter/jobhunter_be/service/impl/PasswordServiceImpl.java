package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.entity.PasswordResetToken;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.InvalidResetPasswordTokenException;
import com.jobhunter.jobhunter_be.repository.PasswordResetTokenRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IPasswordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements IPasswordService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;
    @Value("${frontend.reset-password-path:/reset-password}")
    private String frontendResetPasswordPath;

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Do not disclose account existence.
            log.info("Password reset requested for non-existing email: {}", email);
            return;
        }

        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(15, ChronoUnit.MINUTES);

        PasswordResetToken resetToken = passwordResetTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(token);
                    existingToken.setExpiryDate(expiry);
                    return existingToken;
                })
                .orElse(
                        PasswordResetToken.builder()
                                .user(user)
                                .token(token)
                                .expiryDate(expiry)
                                .build()
                );

        passwordResetTokenRepository.save(resetToken);

        String resetPath = frontendResetPasswordPath.startsWith("/") ? frontendResetPasswordPath : "/" + frontendResetPasswordPath;
        String resetLink = frontendBaseUrl + resetPath + "?token=" + token;
        sendLinkResetPassword(resetLink, user.getEmail());
    }

    @Async
    public void sendLinkResetPassword(String resetLink, String username) {
        log.info("Password reset link for {}: {}", username, resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidResetPasswordTokenException("Invalid reset password token"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidResetPasswordTokenException("Invalid reset password token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}
