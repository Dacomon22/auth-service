package com.david.authservice.persistence.adapter;

import com.david.authservice.model.UserDTO;
import com.david.authservice.persistence.entity.UserEntity;
import com.david.authservice.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private AuthRepositoryAdapter authRepositoryAdapter;

    @Test
    void shouldReturnUserDtoWhenUserExists() {
        // Given
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("david");
        entity.setPassword("encodedPassword");

        when(userJpaRepository.findByUsername("david")).thenReturn(Optional.of(entity));

        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByUsername("david");

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("david", result.get().getUsername());
        assertEquals("encodedPassword", result.get().getPassword());
    }

    @Test
    void shouldReturnEmptyWhenUserDoesNotExist() {
        // Given
        when(userJpaRepository.findByUsername("david")).thenReturn(Optional.empty());

        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByUsername("david");

        // Then
        assertFalse(result.isPresent());
    }

}
