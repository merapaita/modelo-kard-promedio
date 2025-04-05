package com.rosist.kardex.security4.service;

import com.rosist.kardex.security4.dto.SaveUser;
import com.rosist.kardex.security4.model.User;
import jakarta.validation.Valid;

import java.util.Optional;

public interface UserService {
    User registerOneCustomer(@Valid SaveUser newUser);
    Optional<User> findOneByUsername(String username);
}
