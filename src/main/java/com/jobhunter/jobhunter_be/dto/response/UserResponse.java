package com.jobhunter.jobhunter_be.dto.response;

import com.jobhunter.jobhunter_be.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String fullname;
    private String email;
    private Role role;
    private String avatar;
}
