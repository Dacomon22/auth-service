package com.david.authservice.config;

import com.david.authservice.persistence.AuthRepository;
import com.david.authservice.service.AuthService;
import com.david.authservice.service.TokenService;
import com.david.authservice.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {

    @Bean
    public AuthService authService(PasswordEncoder passwordEncoder,
                                   AuthRepository authRepository,
                                   TokenService tokenService) {
        return new AuthServiceImpl(passwordEncoder, authRepository, tokenService);
    }
}
