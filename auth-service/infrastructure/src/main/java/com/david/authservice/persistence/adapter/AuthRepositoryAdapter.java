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
    public Optional<UserDTO> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDto);
    }

    private UserDTO toDto(UserEntity entity) {
        return new UserDTO(
                entity.getEmail(),
                entity.getPassword(),
                entity.getId()
        );
    }
}
