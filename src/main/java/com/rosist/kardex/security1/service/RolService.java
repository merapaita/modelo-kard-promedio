//package com.rosist.kardex.security1.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.rosist.kardex.security1.model.Rol;
//import com.rosist.kardex.security1.repo.IRolRepo;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class RolService {
//
//    @Autowired
//    IRolRepo repo;
//
//    public Optional<Rol> getByRolNombre(String rolNombre){
//        return repo.findByNombre(rolNombre);
//    }
//
//    public void save(Rol rol){
//        repo.save(rol);
//    }
//}
