//package com.rosist.kardex.security1.service;
//
////import com.tutorial.crud.security.entity.Usuario;
////import com.tutorial.crud.security.repository.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.rosist.kardex.security1.model.Usuario;
//import com.rosist.kardex.security1.repo.IUsuarioRepo;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class UsuarioService {
//
//    @Autowired
//    IUsuarioRepo repo;
//
//    public Optional<Usuario> getByUsername(String username){
//        return repo.findByUsername(username);
//    }
//
//    public Optional<Usuario> getByUsernameOrEmail(String usernameOrEmail){
//        return repo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//    }
//
//    public Optional<Usuario> getByTokenPassword(String tokenPassword){
//        return repo.findByTokenPassword(tokenPassword);
//    }
//
//    public boolean existsByUsername(String username){
//        return repo.existsByUsername(username);
//    }
//
//    public boolean existsByEmail(String email){
//        return repo.existsByEmail(email);
//    }
//
//    public Usuario save(Usuario usuario){
//        return repo.save(usuario);
//    }
//    
//}