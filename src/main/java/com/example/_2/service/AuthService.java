package com.example._2.service;

import com.example._2.dto.AuthResponse;
import com.example._2.dto.LoginRequest;
import com.example._2.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
