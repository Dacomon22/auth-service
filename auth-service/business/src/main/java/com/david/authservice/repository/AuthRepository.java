package com.david.authservice.repository;

import com.david.authservice.model.UserDTO;

import java.util.Optional;

public interface AuthRepository {

    Optional<UserDTO> findByUsername(String username);
}
