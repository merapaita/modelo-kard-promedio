//package com.rosist.kardex.security2.service;
//
//import com.rosist.kardex.security2.entity.Usuario;
//import com.rosist.kardex.security2.entity.UsuarioPrincipal;
//import com.rosist.kardex.security2.repository.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    UsuarioRepository usuarioRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String nombreOrEmail) throws UsernameNotFoundException {
//        Usuario usuario = usuarioRepository.findByNombreUsuarioOrEmail(nombreOrEmail, nombreOrEmail).get();
//        return UsuarioPrincipal.build(usuario);
//    }
//}
