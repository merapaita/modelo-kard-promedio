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

import com.rosist.kardex.dto.ItepedidoDto;
import com.rosist.kardex.exception.ModelNotFoundException;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.service.IItepedidoService;

@RestController
@RequestMapping("/itepec")
@CrossOrigin(origins="*")
public class ItepedidoController {

	@Autowired
	private IItepedidoService service;
	
	private static final Logger log = LoggerFactory.getLogger(ItepedidoController.class);
	
	@GetMapping
	public ResponseEntity<List<Itepedido>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}
	
	@GetMapping("/listaPorPecosa/{idPecosa}")
	public ResponseEntity<List<Itepedido>> listaPorOrden(@PathVariable("idPecosa") Integer idPecosa) throws Exception{
		log.info("listaPorInventario...idPecosa:" + idPecosa);
		return new ResponseEntity<>(service.listaPorPedido(idPecosa), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Itepedido> registrar(@Valid @RequestBody ItepedidoDto itepec) throws Exception {
		Itepedido obj = service.registrarItepedido(itepec);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdItepedido()).toUri();
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
	public EntityModel<Itepedido> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Itepedido obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Itepedido> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}
	
}
