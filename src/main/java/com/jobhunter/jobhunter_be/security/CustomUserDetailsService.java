package com.jobhunter.jobhunter_be.security;


import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("Login failed: user '{}' not found", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        return new CustomUserDetails(user);
    }
}
