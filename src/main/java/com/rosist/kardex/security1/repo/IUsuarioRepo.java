//package com.rosist.kardex.security1.repo;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.rosist.kardex.repo.IGenericRepo;
//import com.rosist.kardex.security1.model.Usuario;
//
//@Repository
//public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {
//    Optional<Usuario> findByUsername(String username);
//    Optional<Usuario> findByEmail(String email);
////    Optional<Usuario> findByUsernameOrEmail(String username, String email);
//    Optional<Usuario> findByTokenPassword(String tokenPassword);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
//}
