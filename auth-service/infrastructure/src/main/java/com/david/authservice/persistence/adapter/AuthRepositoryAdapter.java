package com.david.authservice.persistence.adapter;

import com.david.authservice.model.UserDTO;
import com.david.authservice.persistence.AuthRepository;
import com.david.authservice.persistence.entity.UserEntity;
import com.david.authservice.persistence.jpa.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class AuthRepositoryAdapter implements AuthRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(this::toDto);
    }

    private UserDTO toDto(UserEntity entity) {
        return new UserDTO(
                entity.getUsername(),
                entity.getPassword(),
                entity.getId()
        );
    }
}
