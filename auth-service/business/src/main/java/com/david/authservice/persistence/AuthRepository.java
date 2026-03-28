package com.david.authservice.persistence;

import com.david.authservice.model.UserDTO;

import java.util.Optional;

public interface AuthRepository {

    Optional<UserDTO> findByEmail(String email);
}
