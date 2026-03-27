package com.david.authservice.persistence.jpa;

import com.david.authservice.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserJpaRepositoryIT {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("david");
        user.setPassword("encodedPassword");

        userJpaRepository.save(user);

        // When
        Optional<UserEntity> result = userJpaRepository.findByUsername("david");

        // Then
        assertTrue(result.isPresent());
        assertEquals("david", result.get().getUsername());
        assertEquals("encodedPassword", result.get().getPassword());
    }

    @Test
    @DisplayName("Should return empty when username does not exist")
    void shouldReturnEmptyWhenUsernameDoesNotExist() {
        // When
        Optional<UserEntity> result = userJpaRepository.findByUsername("unknown");

        // Then
        assertFalse(result.isPresent());
    }
}
