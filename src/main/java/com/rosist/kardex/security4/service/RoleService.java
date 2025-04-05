package com.rosist.kardex.security4.service;

import com.rosist.kardex.security4.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}