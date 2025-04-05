//package com.rosist.kardex.security1.service;
//
////import com.tutorial.crud.security.entity.Usuario;
////import com.tutorial.crud.security.entity.UsuarioPrincipal;
////import com.tutorial.crud.security.repository.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.rosist.kardex.security1.model.Usuario;
//import com.rosist.kardex.security1.model.UsuarioDetails;
//import com.rosist.kardex.security1.repo.IUsuarioRepo;
//
//@Service
//public class UsuarioDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    IUsuarioRepo usuarioRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Usuario usuario = usuarioRepository.findByUsername(username).get();
//        return UsuarioDetails.build(usuario);
//    }
//}
