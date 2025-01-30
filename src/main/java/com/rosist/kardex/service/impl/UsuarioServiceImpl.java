//package com.rosist.kardex.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.rosist.kardex.model.Usuario;
//import com.rosist.kardex.repo.IGenericRepo;
//import com.rosist.kardex.repo.IUsuarioRepo;
//import com.rosist.kardex.service.IUsuarioService;
//
//@Service
//public class UsuarioServiceImpl extends CRUDImpl<Usuario, Integer> implements IUsuarioService, UserDetailsService {
//
//	@Autowired
//	private IUsuarioRepo repo;
//	
//	@Override
//	public Integer getNewId() {
//		int id = repo.getNewId();
//		return id;
//	}
//
//	@Override
//	protected IGenericRepo<Usuario, Integer> getRepo() {
//		return repo;
//	}
//	
//	@Override
//	public Page<Usuario> listarPageable(Pageable page) {
//		return repo.findAll(page);
//	}
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Usuario usuario = repo.findOneByUsername(username);
//		
//		if(usuario == null) {
//			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
//		}
//		
//		List<GrantedAuthority> roles = new ArrayList<>();
//		
//		usuario.getRoles().forEach(rol -> {
//			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
//		});
//				
//		UserDetails ud = new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true, roles);
//		
//		return ud;
//	}
//	
////	@Transactional
////	@Override
////	public Usuario registrarRoles(Usuario usuario) throws Exception {
//		
////		usuario.getRoles().forEach(det -> Consulta(dto.getConsulta()));
////		repo.save(dto.getConsulta());
////		dto.getLstExamen().forEach(ex -> ceRepo.registrar(dto.getConsulta().getIdConsulta(), ex.getIdExamen()));
////		return dto.getConsulta();
////	}
//
//
//}
