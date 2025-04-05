//package com.rosist.kardex.security1.controller;
//
//import com.rosist.kardex.exception.ResourceNotFoundException;
//import com.rosist.kardex.model.Agrupo;
////import com.tutorial.crud.dto.Mensaje;
//import com.rosist.kardex.security1.dto.JwtDto;
//import com.rosist.kardex.security1.dto.LoginUsuario;
//import com.rosist.kardex.security1.dto.Mensaje;
//import com.rosist.kardex.security1.dto.UsuarioNuevo;
//import com.rosist.kardex.security1.model.Rol;
//import com.rosist.kardex.security1.model.Usuario;
////import com.rosist.kardex.security1.enums.RolNombre;
//import com.rosist.kardex.security1.jwt.JwtProvider;
//import com.rosist.kardex.security1.service.RolService;
//import com.rosist.kardex.security1.service.impl.IUsuarioService;
//
//import jakarta.validation.Valid;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.text.ParseException;
//import java.util.HashSet;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin
//public class AuthController {
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    IUsuarioService service;
//
//    @Autowired
//    RolService rolService;
//
//    @Autowired
//    JwtProvider jwtProvider;
//
//    
//	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
//
//	@PostMapping("/nuevo")
//	public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioNuevo usuarioDto) throws Exception {
//		Usuario obj = service.registrar(usuarioDto);
//		//localhost:8080/medicos/1
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdUsuario()).toUri();
//		return ResponseEntity.created(location).build();
//	}
//    
//    @PostMapping("/login")
//    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
//        if(bindingResult.hasErrors())
//            throw new ResourceNotFoundException("campos mal puestos");
//        Authentication authentication =
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtProvider.generateToken(authentication);
//        JwtDto jwtDto = new JwtDto(jwt);
//        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
//        String token = jwtProvider.refreshToken(jwtDto);
//        JwtDto jwt = new JwtDto(token);
//        return new ResponseEntity<>(jwt, HttpStatus.OK);
//    }
//}
