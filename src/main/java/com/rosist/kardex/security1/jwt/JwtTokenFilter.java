//package com.rosist.kardex.security1.jwt;
//
//import com.rosist.kardex.security1.service.UsuarioDetailsServiceImpl;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//
//import java.io.IOException;
//
//@Component
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
//
//    @Autowired
//    JwtProvider jwtProvider;
//
//    @Autowired
//    UsuarioDetailsServiceImpl userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String token = getToken(req);
//            if(token != null && jwtProvider.validateToken(token)){
//                String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
//
//                UsernamePasswordAuthenticationToken auth =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        } catch (Exception e){
//            logger.error("fail en el método doFilter " + e.getMessage());
//        }
//        filterChain.doFilter(req, res);
//    }
//
//    private String getToken(HttpServletRequest request){
//        String header = request.getHeader("Authorization");
//        if(header != null && header.startsWith("Bearer"))
//            return header.replace("Bearer ", "");
//        return null;
//    }
//}