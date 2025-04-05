package com.rosist.kardex.security4.config.filter;

import com.rosist.kardex.exception2.ObjectNotFoundException;
import com.rosist.kardex.security4.model.JwtToken;
import com.rosist.kardex.security4.model.User;
import com.rosist.kardex.security4.repository.JwtTokenRepository;
import com.rosist.kardex.security4.service.JwtService;
import com.rosist.kardex.security4.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtRepository;

    @Autowired
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Obtener authorization header
        // 2. Obtener tojen
        String jwt = jwtService.extractJwtFromRequest(request);
        System.out.println("doFilterIternal...jwt:" + jwt);
        if (!StringUtils.hasText(jwt)) {
            System.out.println("doFilterIternal...jwt: if null");
            filterChain.doFilter(request,response);
            return;
        }
        // 2.1 Obtener token valido y no expirado de la base de datos
        System.out.println("diFilterInternal,,,antes de recuperar token");
        Optional<JwtToken> token = jwtRepository.findByToken(jwt);
        System.out.println("diFilterInternal,,,obteniendo token");
        boolean isValid = validateToken(token);

        if (!isValid) {
            filterChain.doFilter(request,response);
            return;
        }
        //3. Obtener el Subject/username del token esto valida eñ formato del token, firma y fecha expiracion
        String username = jwtService.extractUsername(jwt);

        //4. Setear objeto autenticacion dentro de security contex holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username" + username));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        //5. Ejecutar el registro de filtros
        filterChain.doFilter(request,response);
    }

    private boolean validateToken(Optional<JwtToken> optionalJwtToken) {
        if (!optionalJwtToken.isPresent()) {
            System.out.printf("Token no existe o no fue generado en nuestr sistema");
            return false;
        }
        JwtToken token = optionalJwtToken.get();
        Date now = new Date(System.currentTimeMillis());
        boolean isValid = token.isValid() && token.getExpiration().after(now);
        if (!isValid) {
            System.out.printf("Token invalido");
            updateTokenSttus(token);
        }
        return isValid;
    }

    private void updateTokenSttus(JwtToken token) {
        token.setValid(false);
        jwtRepository.save(token);
    }
}