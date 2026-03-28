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
        user.setEmail("david@test.com");
        user.setPassword("encodedPassword");
        userJpaRepository.save(user);

        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByEmail("david@test.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("david@test.com", result.get().getEmail());
        assertEquals("encodedPassword", result.get().getPassword());
    }

    @Test
    @DisplayName("Should return empty when user does not exist in database")
    void shouldReturnEmptyWhenUserDoesNotExistInDatabase() {
        // When
        Optional<UserDTO> result = authRepositoryAdapter.findByEmail("unknown");

        // Then
        assertFalse(result.isPresent());
    }
}
