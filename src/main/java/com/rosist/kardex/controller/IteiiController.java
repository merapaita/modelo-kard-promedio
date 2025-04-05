package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rosist.kardex.dto.IteiiDto;
import com.rosist.kardex.exception.ModelNotFoundException;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.service.IIteiiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/iteii")
public class IteiiController {

	@Autowired
	private IIteiiService service;
	
	private static final Logger log = LoggerFactory.getLogger(IteiiController.class);

	@GetMapping
	public ResponseEntity<List<Iteii>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}

	@GetMapping("/listaPorInventario/{idInvini}")
	public ResponseEntity<List<Iteii>> listaPorInventario(@PathVariable("idInvini") Integer idInvini) throws Exception{
		return new ResponseEntity<>(service.listaPorInventario(idInvini), HttpStatus.OK);
	}
	
//	@GetMapping("/{id}")
//	public ResponseEntity<Invini> listarPorId(@PathVariable("id") Integer id) throws Exception{
//		Invini obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		return new ResponseEntity<>(obj, HttpStatus.OK);
//	}
	
	@PostMapping
	public ResponseEntity<Iteii> registrar(@Valid @RequestBody IteiiDto iteii) throws Exception {
		Iteii obj = service.registrarIteii(iteii);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdIteii()).toUri();
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
		log.info("eliminar...id:" + id);
		service.eliminarItem(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Iteii> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Iteii obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Iteii> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}
	
}