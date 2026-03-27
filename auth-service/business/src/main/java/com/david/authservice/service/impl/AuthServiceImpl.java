package com.david.authservice.service.impl;

import com.david.authservice.exception.InvalidCredentialsException;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.model.UserDTO;
import com.david.authservice.repository.AuthRepository;
import com.david.authservice.service.AuthService;
import com.david.authservice.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final TokenService tokenService;

    @Override
    public AuthResponse login(LoginRequestDTO request) {
        UserDTO user = authRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String token = tokenService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }

}
