package com.jobhunter.jobhunter_be.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jobhunter.jobhunter_be.util.AuthSerializer;
import lombok.*;

@JsonSerialize(using = AuthSerializer.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse<T> {
    private T data;
    private String token;
    private String refreshToken;
}
