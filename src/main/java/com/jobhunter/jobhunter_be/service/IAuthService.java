package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.LoginRequest;
import com.jobhunter.jobhunter_be.dto.request.RegisterRequest;
import com.jobhunter.jobhunter_be.dto.response.AuthResponse;
import com.jobhunter.jobhunter_be.dto.response.UserResponse;
import com.jobhunter.jobhunter_be.exception.custom.*;

public interface IAuthService {
    public abstract void register(RegisterRequest request) throws UsernameExistedException, RoleNotFoundException;
    public abstract AuthResponse login(LoginRequest request);
    public abstract void logout(String refreshToken) throws RefreshTokenNotFoundException;
    public abstract AuthResponse refreshToken(String refreshToken) throws RefreshTokenNotFoundException, ExpiredRefreshTokenException;
    public abstract UserResponse currentUser(String email) throws NotFoundException;
}
