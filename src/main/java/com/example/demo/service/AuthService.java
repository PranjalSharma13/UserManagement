package com.example.demo.service;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RefreshTokenRequest;
import com.example.demo.payload.response.LoginResponse;
import org.springframework.stereotype.Service;


public interface AuthService {
    public LoginResponse login(LoginRequest loginRequest);
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
