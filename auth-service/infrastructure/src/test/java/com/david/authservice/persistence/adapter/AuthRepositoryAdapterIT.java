package com.david.authservice.persistence.adapter;

import com.david.authservice.model.UserDTO;
import com.david.authservice.persistence.entity.UserEntity;
import com.david.authservice.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthRepositoryAdapterIT {

    @Autowired
    private AuthRepositoryAdapter authRepositoryAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Should return UserDTO when user exists in database")
    void shouldReturnUserDtoWhenUserExistsInDatabase() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("david");
        user.setPassword("encodedPassword");
        userJpaRepository.save(user);

        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByUsername("david");

        // Then
        assertTrue(result.isPresent());
        assertEquals("david", result.get().getUsername());
        assertEquals("encodedPassword", result.get().getPassword());
    }

    @Test
    @DisplayName("Should return empty when user does not exist in database")
    void shouldReturnEmptyWhenUserDoesNotExistInDatabase() {
        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByUsername("unknown");

        // Then
        assertFalse(result.isPresent());
    }
}
