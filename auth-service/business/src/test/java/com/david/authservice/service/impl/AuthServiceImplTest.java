package com.david.authservice.service.impl;

import com.david.authservice.exception.InvalidCredentialsException;
import com.david.authservice.model.AuthResponse;
import com.david.authservice.model.LoginRequestDTO;
import com.david.authservice.model.UserDTO;
import com.david.authservice.repository.AuthRepository;
import com.david.authservice.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("david");
        request.setPassword("plainPassword");

        UserDTO user = new UserDTO();
        user.setUsername("david");
        user.setPassword("encodedPassword");

        when(authRepository.findByUsername("david")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(tokenService.generateToken("david")).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.login(request);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());

        verify(authRepository).findByUsername("david");
        verify(passwordEncoder).matches("plainPassword", "encodedPassword");
        verify(tokenService).generateToken("david");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("david");
        request.setPassword("plainPassword");

        when(authRepository.findByUsername("david")).thenReturn(Optional.empty());

        // When
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        // Then
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("david");
        verify(passwordEncoder, never()).matches(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
        verify(tokenService, never()).generateToken(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("david");
        request.setPassword("wrongPassword");

        UserDTO user = new UserDTO();
        user.setUsername("david");
        user.setPassword("encodedPassword");

        when(authRepository.findByUsername("david")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // When
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        // Then
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("david");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
        verify(tokenService, never()).generateToken(org.mockito.ArgumentMatchers.anyString());
    }

}
