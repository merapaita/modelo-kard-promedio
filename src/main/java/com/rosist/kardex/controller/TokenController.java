//package com.rosist.kardex.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.rosist.kardex.model.Rol;
//import com.rosist.kardex.model.Usuario;
//import com.rosist.kardex.service.IUsuarioService;
//
//@RestController
//@RequestMapping("/tokens")
//public class TokenController {
//
//	@Autowired
//	private ConsumerTokenServices tokenServices;
//	
//	@Autowired
//	private IUsuarioService usuarioService;
//	
//	@Autowired
//	private BCryptPasswordEncoder bcrypt;
//	
//    private static Logger logger = LoggerFactory.getLogger(TokenController.class);
//    
//	@GetMapping("/anular/{tokenId:.*}")
//	public void revocarToken(@PathVariable("tokenId") String token) {
//		tokenServices.revokeToken(token);
//	}
//	
//	// KeyCloak
//	@PostMapping(value = "/usuario/agregar")
//	public ResponseEntity<Boolean> createUser(@RequestBody Usuario usuario) throws Exception {
//		logger.info(usuario.toString());
//		usuario.setIdUsuario(usuarioService.getNewId());
//		usuario.setPassword(bcrypt.encode(usuario.getPassword()));
//		usuario.setEnabled(true);
//		
//		List<Rol> lrol = new ArrayList<>();
//		Rol rol = new Rol();
//		rol.setIdRol(4);
//		lrol.add(rol);
//		usuario.setRoles(lrol);
//		logger.info(usuario.toString());
//		Usuario _usuario = usuarioService.registrar(usuario);
//		boolean rpta = false;
//		if (_usuario!=null) {
//			rpta = true;
//		}
////		boolean rpta = keycloakService.agregarUsuario(usuario);
//		return new ResponseEntity<Boolean>(rpta, HttpStatus.OK);
//	}
//	
//}
