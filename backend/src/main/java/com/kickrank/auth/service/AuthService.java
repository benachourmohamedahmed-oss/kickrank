package com.kickrank.auth.service;

import com.kickrank.auth.dto.AuthResponse;
import com.kickrank.auth.dto.LoginRequest;
import com.kickrank.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
