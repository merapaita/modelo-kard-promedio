//package com.rosist.kardex.controller;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.validation.Valid;
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
//import com.rosist.kardex.model.Rol;
//import com.rosist.kardex.service.IRolService;
//
//@RestController
//@RequestMapping("/rol")
//public class RolController {
//
//	@Autowired
//	private IRolService service;
//	
//    private static Logger logger = LoggerFactory.getLogger(RolController.class);
//    
//	@GetMapping
//	public ResponseEntity<List<Rol>> listar() throws Exception{
//		List<Rol> rols = new ArrayList<>();
//		rols = service.listar();
//		return new ResponseEntity<List<Rol>>(rols, HttpStatus.OK);
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
//	public ResponseEntity<Rol> listarPorId(@PathVariable("id") Integer id) throws Exception{
//		Rol obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		return new ResponseEntity<>(obj, HttpStatus.OK);		
//	}
//	
//	@PostMapping
//	public ResponseEntity<Rol> registrar(@Valid @RequestBody Rol rol) throws Exception {
//		Rol obj = service.registrar(rol);
//		//localhost:8080/medicos/1
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdRol()).toUri();
//		return ResponseEntity.created(location).build();
////		return new ResponseEntity<>(service.registrar(paciente), HttpStatus.CREATED);		// 201
//	}
//
//	@PutMapping
//	public ResponseEntity<Rol> modificar(@Valid @RequestBody Rol rol) throws Exception {
//		return new ResponseEntity<>(service.modificar(rol), HttpStatus.OK);		
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
//		service.eliminar(id);
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
//	
//	@GetMapping("/pageable")
//	public ResponseEntity<Page<Rol>> listarPageable(Pageable pageable) throws Exception{
//		Page<Rol> pacientes = service.listarPageable(pageable);
//		return new ResponseEntity<Page<Rol>>(pacientes, HttpStatus.OK);
//	}
//
//}
