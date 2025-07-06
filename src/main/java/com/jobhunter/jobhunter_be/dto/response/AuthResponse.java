package com.jobhunter.jobhunter_be.dto.response;

import com.jobhunter.jobhunter_be.entity.Role;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String email;
    private String fullName;
    private Set<Role> roles;
    private String token;
    private String refreshToken;
}
