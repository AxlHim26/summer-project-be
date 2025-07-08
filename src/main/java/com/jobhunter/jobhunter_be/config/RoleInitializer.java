package com.jobhunter.jobhunter_be.config;

import com.jobhunter.jobhunter_be.entity.Role;
import com.jobhunter.jobhunter_be.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitializer {

    private final RoleRepository roleRepository;

    private final List<String> defaultRoles = List.of(
            "ROLE_CANDIDATE",
            "ROLE_RECRUITER"
    );

    @PostConstruct
    public void initRoles() {
        for (String roleName : defaultRoles) {
            boolean exists = roleRepository.existsByName(roleName);
            if (!exists) {
                roleRepository.save(new Role(roleName));
                log.info("Created role: {}", roleName);
            } else {
                log.info("Role already exists: {}", roleName);
            }
        }
    }
}
