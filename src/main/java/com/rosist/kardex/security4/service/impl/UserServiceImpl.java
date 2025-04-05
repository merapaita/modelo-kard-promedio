package com.rosist.kardex.security4.service.impl;

import com.rosist.kardex.exception2.InvalidPasswordException;
import com.rosist.kardex.exception2.ObjectNotFoundException;
import com.rosist.kardex.security4.dto.SaveUser;
import com.rosist.kardex.security4.model.Role;
import com.rosist.kardex.security4.model.User;
import com.rosist.kardex.security4.repository.UserRepository;
import com.rosist.kardex.security4.service.RoleService;
import com.rosist.kardex.security4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public User registerOneCustomer(SaveUser newUser) {

        validatePassword(newUser);
        System.out.println("despues de validar el password");
        User user = new User();
        user.setName(newUser.getName());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        System.out.println("antes de buscar los roles");

        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Role not fount. Default role"));
        System.out.println("roles encontrados:" + defaultRole);
        user.setRole(defaultRole);     //Role.CUSTOMER
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(SaveUser dto) {

        if(!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

        if(!dto.getPassword().equals(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }
}
