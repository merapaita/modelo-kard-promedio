package com.rosist.kardex.security4.service.impl;

import com.rosist.kardex.security4.model.Role;
import com.rosist.kardex.security4.repository.RoleRepository;
import com.rosist.kardex.security4.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Value("${security.default.role}")
    private String defaultRole;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findDefaultRole() {
        System.out.println("defaultRole:" + defaultRole);
        return roleRepository.findByName(defaultRole);
    }
}
