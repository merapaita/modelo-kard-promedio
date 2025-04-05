//package com.rosist.kardex.controller;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.validation.Valid;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import com.rosist.kardex.exception.ModelNotFoundException;
//import com.rosist.kardex.model.Usuario;
//import com.rosist.kardex.service.IUsuarioService;
//
//@RestController
//@RequestMapping("/usuario")
//public class UsuarioController {
//
//	@Autowired
//	private IUsuarioService service;
//	
//    private static Logger logger = LoggerFactory.getLogger(UsuarioController.class);
//    
//	@GetMapping
//	public ResponseEntity<List<Usuario>> listar() throws Exception{
//		List<Usuario> rols = new ArrayList<>();
//		rols = service.listar();
//		return new ResponseEntity<List<Usuario>>(rols, HttpStatus.OK);
//	}
//	
////	@PostMapping("/usuario")
////	public ResponseEntity<List<Rol>> listar(@RequestBody String nombre) throws Exception {
////		List<Rol> rols = new ArrayList<>();
////		rols = service.listarRolPorUsuario(nombre);
////		return new ResponseEntity<List<Rol>>(rols, HttpStatus.OK);
////	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<Usuario> listarPorId(@PathVariable("id") Integer id) throws Exception{
//		Usuario obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		return new ResponseEntity<>(obj, HttpStatus.OK);		
//	}
//	
//	@PostMapping
//	public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) throws Exception {
//		Usuario obj = service.registrar(usuario);
//		//localhost:8080/medicos/1
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdUsuario()).toUri();
//		return ResponseEntity.created(location).build();
////		return new ResponseEntity<>(service.registrar(paciente), HttpStatus.CREATED);		// 201
//	}
//
//	@PutMapping
//	public ResponseEntity<Usuario> modificar(@Valid @RequestBody Usuario usuario) throws Exception {
//		logger.info("usuario.-> " + usuario.toString());
//		return new ResponseEntity<>(service.modificar(usuario), HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
//		service.eliminar(id);
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
//	
//	@GetMapping("/pageable")
//	public ResponseEntity<Page<Usuario>> listarPageable(Pageable pageable) throws Exception{
//		Page<Usuario> pacientes = service.listarPageable(pageable);
//		return new ResponseEntity<Page<Usuario>>(pacientes, HttpStatus.OK);
//	}
//
//}