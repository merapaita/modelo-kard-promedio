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
//import com.rosist.kardex.model.Menu;
//import com.rosist.kardex.service.IMenuService;
//
//@RestController
//@RequestMapping("/menu")
//public class MenuController {
//
//	@Autowired
//	private IMenuService service;
//	
//    private static Logger logger = LoggerFactory.getLogger(MenuController.class);
//    
//	@GetMapping
//	public ResponseEntity<List<Menu>> listar() throws Exception{
//		List<Menu> menus = new ArrayList<>();
//		menus = service.lista();
//		return new ResponseEntity<List<Menu>>(menus, HttpStatus.OK);
//	}
//	
//	@PostMapping("/usuario")
//	public ResponseEntity<List<Menu>> listar(@RequestBody String nombre) throws Exception{
//		List<Menu> menus = new ArrayList<>();
//		menus = service.listarMenuPorUsuario(nombre);
//		return new ResponseEntity<List<Menu>>(menus, HttpStatus.OK);
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<Menu> listarPorId(@PathVariable("id") Integer id) throws Exception{
//		Menu obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		return new ResponseEntity<>(obj, HttpStatus.OK);		
//	}
//	
//	@PostMapping
//	public ResponseEntity<Menu> registrar(@Valid @RequestBody Menu menu) throws Exception {
//		logger.info("menu:" + menu);
//		Menu obj = service.registrar(menu);
//		//localhost:8080/medicos/1
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMenu()).toUri();
//		return ResponseEntity.created(location).build();
////		return new ResponseEntity<>(service.registrar(paciente), HttpStatus.CREATED);		// 201
//	}
//
//	@PutMapping
//	public ResponseEntity<Menu> modificar(@Valid @RequestBody Menu menu) throws Exception {
//		return new ResponseEntity<>(service.modificar(menu), HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
//		service.eliminar(id);
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
//	
//	@GetMapping("/pageable")
//	public ResponseEntity<Page<Menu>> listarPageable(Pageable pageable) throws Exception{
//		Page<Menu> pacientes = service.listarPageable(pageable);
//		return new ResponseEntity<Page<Menu>>(pacientes, HttpStatus.OK);
//	}
//
//}
