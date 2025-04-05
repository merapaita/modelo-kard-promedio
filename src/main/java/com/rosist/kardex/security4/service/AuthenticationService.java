package com.rosist.kardex.security4.service;

import com.rosist.kardex.exception2.ObjectNotFoundException;
import com.rosist.kardex.security4.dto.AuthenticationRequest;
import com.rosist.kardex.security4.dto.AuthenticationResponse;
import com.rosist.kardex.security4.dto.RegisteredUser;
import com.rosist.kardex.security4.dto.SaveUser;
import com.rosist.kardex.security4.model.JwtToken;
import com.rosist.kardex.security4.model.User;
import com.rosist.kardex.security4.repository.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        System.out.println("antes de registerOneCustomer");
        User user = userService.registerOneCustomer(newUser);
        System.out.println("antes de generar el token");
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        System.out.println("despues de generar el token");
        saveUserToken(user, jwt);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name",user.getName());
        extraClaims.put("role",user.getRole().getName());
        extraClaims.put("authorities",user.getAuthorities());
        return extraClaims;
    }

    public AuthenticationResponse login(@Valid AuthenticationRequest authenticationRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),authenticationRequest.getPassword());
        authenticationManager.authenticate(authentication);
        UserDetails user = userService.findOneByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));
        saveUserToken((User)user, jwt);

        AuthenticationResponse autResponse = new AuthenticationResponse();
        autResponse.setJwt(jwt);

        return autResponse;

    }

    private void saveUserToken(User user, String jwt) {
        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);
        jwtRepository.save(token);
        System.out.println("activando token");
    }
    public boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User findLoggedInUser() {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        return userService.findOneByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

    }

    public void logout(HttpServletRequest request) {
        System.out.println("pretendiendo.....");
        String jwt = jwtService.extractJwtFromRequest(request);
        if (jwt == null || !StringUtils.hasText(jwt)) return;
        Optional<JwtToken> token = jwtRepository.findByToken(jwt);
        if (token.isPresent() && token.get().isValid()) {
            token.get().setValid(false);
            System.out.println("desactivando token");
            jwtRepository.save(token.get());
            System.out.println("desactivando token");
        }
    }
}