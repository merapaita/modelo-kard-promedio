package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rosist.kardex.exception.ModelNotFoundException;
import com.rosist.kardex.model.Proveedor;
import com.rosist.kardex.repo.IProveedorRepo;
import com.rosist.kardex.service.IProveedorService;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {

	@Autowired
	private IProveedorService service;
	@Autowired
	private IProveedorRepo repo;
	
    private static Logger logger = LoggerFactory.getLogger(ProveedorController.class);
    
	@GetMapping
	public ResponseEntity<List<Proveedor>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}
    
	@GetMapping("/pageable")
	  public ResponseEntity<Map<String, Object>> getAllProveedor(
		        @RequestParam(value="nombre", required = false) String nombre,
		        @RequestParam(value="page", defaultValue = "0") int page,
		        @RequestParam(value="size", defaultValue = "10") int size
			  ) {
		
		List<Proveedor> proveedor = new ArrayList<Proveedor>();
		Pageable paging = PageRequest.of(page, size);
		
		Page<Proveedor> pageTuts;
//		logger.info("getAllSecfun...periodo:" + periodo);
//		logger.info("getAllSecfun...secfun:" + cadena);
		if (nombre != null) {
			pageTuts = repo.listaPorNombre(nombre, paging);
		}
		else {
			pageTuts = repo.findAll(paging);
		}

		proveedor = pageTuts.getContent();

		logger.info(pageTuts.toString());
		Map<String, Object> response = new HashMap<>();
		response.put("content", proveedor);
		response.put("number", pageTuts.getNumber());
		response.put("totalElements", pageTuts.getTotalElements());
		response.put("totalPages", pageTuts.getTotalPages());
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Proveedor> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Proveedor obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		return new ResponseEntity<>(obj, HttpStatus.OK);		
	}
	
	@GetMapping("/ruc/{ruc}")
	public ResponseEntity<Proveedor> listarPorRuc(@PathVariable("ruc") String ruc) throws Exception{
		Proveedor obj = service.buscaPorRUC(ruc);
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + ruc);
//		}
		return new ResponseEntity<>(obj, HttpStatus.OK);		
	}

	@PostMapping
	public ResponseEntity<Proveedor> registrar(@Valid @RequestBody Proveedor proveedor) throws Exception {
		Proveedor obj = service.registrarTransaccion(proveedor);
		//localhost:8080/medicos/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdProveedor()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(paciente), HttpStatus.CREATED);		// 201
	}
	
	@PutMapping
	public ResponseEntity<Proveedor> modificar(@Valid @RequestBody Proveedor proveedor) throws Exception {
	logger.info("midificar...proveedor,.> " + proveedor);
		return new ResponseEntity<>(service.modificaProveedor(proveedor), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Proveedor> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Proveedor obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		EntityModel<Proveedor> recurso = EntityModel.of(obj);
		//localhost:8080/medicos/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("Paciente-recurso1"));
		recurso.add(link2.withRel("Paciente-recurso2"));
		
		return recurso;
		
	}
	
//	@GetMapping("/pageable")
//	public ResponseEntity<Page<Proveedor>> listarPageable(Pageable pageable) throws Exception{
////		logger.info("listaPageable...pageable.->" + pageable.toString());
//		Page<Proveedor> proveedor = service.listarPageable(pageable);
////		logger.info("listaPageable...paciente.->" + pacientes.toString());
//		return new ResponseEntity<Page<Proveedor>>(proveedor, HttpStatus.OK);
//	}
	
}