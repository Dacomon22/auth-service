package com.david.authservice.service.impl;

import com.david.authservice.exception.InvalidCredentialsException;
import com.david.authservice.exception.SsoValidationException;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.model.UserDTO;
import com.david.authservice.persistence.AuthRepository;
import com.david.authservice.service.AuthService;
import com.david.authservice.service.TokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;


@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final TokenService tokenService;

    @Override
    public AuthResponse login(LoginRequestDTO request) {

        log.info("Starting authentication process for username: {}", request.getEmail());

        UserDTO user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Authentication failed: invalid password for user -> {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        log.info("Password validated successfully for user: {}", request.getEmail());

        String token = tokenService.generateToken(user.getEmail());

        log.info("JWT token generated successfully for user: {}", request.getEmail());

        return new AuthResponse(token);
    }


    @Override
    public AuthResponse callback(String code,String username) {

            if (code == null || code.trim().isEmpty()) {
                throw new SsoValidationException("Missing authorization code");
            }

            if (!"valid-code".equals(code)) {
                throw new SsoValidationException("Invalid authorization code");
            }

            String token = tokenService.generateToken(username);

            return new AuthResponse(token);
        }


}
