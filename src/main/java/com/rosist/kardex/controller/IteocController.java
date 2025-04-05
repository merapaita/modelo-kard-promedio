package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rosist.kardex.dto.IteocDto;
import com.rosist.kardex.exception.ModelNotFoundException;
import com.rosist.kardex.model.Iteoc;
import com.rosist.kardex.service.IIteocService;

@RestController
@RequestMapping("/iteoc")
@CrossOrigin(origins="*")
public class IteocController {

	@Autowired
	private IIteocService service;
	
	private static final Logger log = LoggerFactory.getLogger(IteocController.class);
	
	@GetMapping
	public ResponseEntity<List<Iteoc>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}
	
	@GetMapping("/listaPorOrdcom/{idOrdcom}")
	public ResponseEntity<List<Iteoc>> listaPorOrden(@PathVariable("idOrdcom") Integer idOrdcom) throws Exception{
		return new ResponseEntity<>(service.listaPorOrden(idOrdcom), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Iteoc> registrar(@Valid @RequestBody IteocDto iteoc) throws Exception {
		Iteoc obj = service.registrarIteoc(iteoc);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdIteoc()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

//	@PutMapping
//	public ResponseEntity<Invini> modificar(@Valid @RequestBody Invini invini) throws Exception {
//		log.info("modificar...invini: " + invini);
//		Invini obj = service.registrarInvini(invini);
//		//localhost:8080/promotors/1
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdInvini()).toUri();
//		return ResponseEntity.created(location).build();
////		return new ResponseEntity<>(service.modificar(invini), HttpStatus.OK);		
//	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminarItem(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Iteoc> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Iteoc obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Iteoc> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}
	
}
