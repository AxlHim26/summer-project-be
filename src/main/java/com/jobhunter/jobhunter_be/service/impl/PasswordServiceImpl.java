package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.entity.PasswordResetToken;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.InvalidResetPasswordTokenException;
import com.jobhunter.jobhunter_be.repository.PasswordResetTokenRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IPasswordService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PasswordServiceImpl implements IPasswordService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

        String resetLink = "http://localhost:3000/api/password/reset?token=" + token;
        sendLinkResetPassword(resetLink, user.getEmail());
    }

    @Async
    public void sendLinkResetPassword(String resetLink, String username) {
        //
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
