//package com.rosist.crudmarco.sec.service;
//
//import com.rosist.crudmarco.sec.entity.Rol;
//import com.rosist.crudmarco.sec.enums.RolNombre;
//import com.rosist.crudmarco.sec.repository.RolRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class RolService {
//
//    @Autowired
//    RolRepository rolRepository;
//
//    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
//        return rolRepository.findByRolNombre(rolNombre);
//    }
//
//    public void save(Rol rol){
//        rolRepository.save(rol);
//    }
//}
