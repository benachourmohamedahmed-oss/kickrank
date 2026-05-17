package com.kickrank.auth.service.impl;

import com.kickrank.auth.dto.AuthResponse;
import com.kickrank.auth.dto.LoginRequest;
import com.kickrank.auth.dto.RegisterRequest;
import com.kickrank.auth.service.AuthService;
import com.kickrank.common.exception.BadRequestException;
import com.kickrank.security.JwtService;
import com.kickrank.user.entity.User;
import com.kickrank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already used");
        }

        var user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        return toResponse(user, jwtService.generateToken(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
        );
        var user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        return toResponse(user, jwtService.generateToken(user));
    }

    private AuthResponse toResponse(User user, String token) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getElo(),
                user.isOrganizerVerified(),
                user.isObserverVerified(),
                user.getRoles()
        );
    }
}
