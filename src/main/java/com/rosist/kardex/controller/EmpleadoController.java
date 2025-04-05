package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
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
import com.rosist.kardex.model.Empleado;
import com.rosist.kardex.repo.IEmpleadoRepo;
import com.rosist.kardex.service.IEmpleadoService;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

	@Autowired
	private IEmpleadoService service;
	
	@Autowired
	private IEmpleadoRepo repo;
	
    private static Logger logger = LoggerFactory.getLogger(EmpleadoController.class);
    
	@GetMapping
	public ResponseEntity<List<Empleado>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}
    
// este codigo si funciona	
//	@GetMapping("/pageable")
//	public ResponseEntity<Page<Empleado>> listarPageable(Pageable pageable) throws Exception{
//		Page<Empleado> empleado = service.listarPageable(pageable);
//		return new ResponseEntity<Page<Empleado>>(empleado, HttpStatus.OK);
//	}

//  tambien funciona
//	 @GetMapping("/pageable")
//	  public ResponseEntity<List<Empleado>> getAllEmpleados(@RequestParam(value="nombre", required = false) String nombre) throws Exception {
//	      List<Empleado> empleados = new ArrayList<Empleado>();
//	      if (nombre == null)
//	    	  repo.findAll().forEach(empleados::add);
//	      else
//	    	  repo.findByNombre(nombre).forEach(empleados::add);
//
//	      if (empleados.isEmpty()) {
//	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	      }
//	      return new ResponseEntity<>(empleados, HttpStatus.OK);
//	  }
	

	@GetMapping("/pageable")
	  public ResponseEntity<Map<String, Object>> getAllEmpleados(
	        @RequestParam(value="nombre", required = false) String nombre,
	        @RequestParam(value="page", defaultValue = "0") int page,
	        @RequestParam(value="size", defaultValue = "10") int size
	      ) {

	      List<Empleado> empleados = new ArrayList<Empleado>();
	      Pageable paging = PageRequest.of(page, size);
	      
	      Page<Empleado> pageTuts;
	      logger.info("getAllEmpleados...nombre:" + nombre);
	      if (nombre == null)
	        pageTuts = repo.findAll(paging);
	      else
	        pageTuts = repo.findByNombre(nombre, paging);

	      empleados = pageTuts.getContent();

	      Map<String, Object> response = new HashMap<>();
	      response.put("content", empleados);
	      response.put("number", pageTuts.getNumber());
	      response.put("totalElements", pageTuts.getTotalElements());
	      response.put("totalPages", pageTuts.getTotalPages());

	      return new ResponseEntity<>(response, HttpStatus.OK);
	  }
	
	@GetMapping("/{id}")
	public ResponseEntity<Empleado> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Empleado obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		return new ResponseEntity<>(obj, HttpStatus.OK);		
	}

	@PostMapping
	public ResponseEntity<Empleado> registrar(@Valid @RequestBody Empleado empleado) throws Exception {
		Empleado obj = service.registrar(empleado);
//		Empleado obj = service.registrarTransaccion(empleado);
		//localhost:8080/medicos/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdEmpleado()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(paciente), HttpStatus.CREATED);		// 201
	}
	
	@PutMapping
	public ResponseEntity<Empleado> modificar(@Valid @RequestBody Empleado empleado) throws Exception {
	logger.info("midificar...empleado,.> " + empleado);
	return new ResponseEntity<>(service.modificar(empleado), HttpStatus.OK);
//	return new ResponseEntity<>(service.modificaEmpleado(empleado), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Empleado> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Empleado obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		EntityModel<Empleado> recurso = EntityModel.of(obj);
		//localhost:8080/medicos/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("Paciente-recurso1"));
		recurso.add(link2.withRel("Paciente-recurso2"));
		
		return recurso;
		
	}
		
}