//package com.rosist.crudmarco.sec.service;
//
//import com.rosist.crudmarco.sec.entity.Usuario;
//import com.rosist.crudmarco.sec.repository.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class UsuarioService {
//
//    @Autowired
//    UsuarioRepository usuarioRepository;
//
//    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
//        return usuarioRepository.findByNombreUsuario(nombreUsuario);
//    }
//
//    public Optional<Usuario> getByNombreUsuarioOrEmail(String nombreOrEmail){
//        return usuarioRepository.findByNombreUsuarioOrEmail(nombreOrEmail, nombreOrEmail);
//    }
//
//    public Optional<Usuario> getByTokenPassword(String tokenPassword){
//        return usuarioRepository.findByTokenPassword(tokenPassword);
//    }
//
//    public boolean existsByNombreUsuario(String nombreUsuario){
//        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
//    }
//
//    public boolean existsByEmail(String email){
//        return usuarioRepository.existsByEmail(email);
//    }
//
//    public void save(Usuario usuario){
//        usuarioRepository.save(usuario);
//    }
//}
