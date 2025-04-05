//package com.rosist.kardex.security1.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.rosist.kardex.exception.ResourceNotFoundException;
//import com.rosist.kardex.repo.IGenericRepo;
//import com.rosist.kardex.security1.dto.UsuarioNuevo;
//import com.rosist.kardex.security1.model.Rol;
//import com.rosist.kardex.security1.model.Usuario;
//import com.rosist.kardex.security1.repo.IUsuarioRepo;
//import com.rosist.kardex.service.impl.CRUDImpl;
//
//@Service
//public class UsuarioServiceImpl extends CRUDImpl<Usuario, Integer> implements IUsuarioService{
//
//	@Autowired
//	private IUsuarioRepo repo;
//	
//	@Override
//	protected IGenericRepo<Usuario, Integer> getRepo() {
//		return repo;
//	}
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
//
//	public Usuario registrar(UsuarioNuevo usuarioDto) throws Exception {
//log.info("usuarioDto:" + usuarioDto);
//		Usuario usuario = repo.findByUsername(usuarioDto.getUsername()).orElse(null);
//		if (usuario!=null) {
//			throw new ResourceNotFoundException("Usuario ya existe");
//		}
//		usuario = repo.findByEmail(usuarioDto.getEmail()).orElse(null);
//		if (usuario!=null) {
//			throw new ResourceNotFoundException("Email ya existe");
//		}
//		
//		usuario = new Usuario();
//		usuario.setUsername(usuarioDto.getUsername());
//		usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
//		usuario.setNombre(usuarioDto.getNombre());
//		usuario.setEmail(usuarioDto.getEmail());
//		usuario.setRoles(usuarioDto.getRoles());
//		return repo.save(usuario);
//	}
//
//}