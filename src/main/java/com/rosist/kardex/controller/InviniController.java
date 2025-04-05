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
import org.springframework.http.MediaType;
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
import com.rosist.kardex.model.Invini;
import com.rosist.kardex.repo.IInviniRepo;
import com.rosist.kardex.service.IInviniService;

@RestController
@RequestMapping("/invini")
public class InviniController {

	@Autowired
	private IInviniService service;
	
	@Autowired
	private IInviniRepo repo;
	
	private static final Logger log = LoggerFactory.getLogger(InviniController.class);

	@GetMapping
	public ResponseEntity<List<Invini>> listar() throws Exception{
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}

	@GetMapping("/pageable")
	public ResponseEntity<Map<String, Object>> getAllSecfun(
			@RequestParam(value="periodo", required = false) Integer periodo,
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = "10") int size
			) {

		List<Invini> invini = new ArrayList<Invini>();
		Pageable paging = PageRequest.of(page, size);
		
		Page<Invini> pageTuts;
		if (periodo == null) periodo = LocalDate.now().getYear();
			pageTuts = repo.listaInv(periodo, paging);
		
		invini = pageTuts.getContent();

		Map<String, Object> response = new HashMap<>();
		response.put("content", invini);
		response.put("number", pageTuts.getNumber());
		response.put("totalElements", pageTuts.getTotalElements());
		response.put("totalPages", pageTuts.getTotalPages());
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Invini> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Invini obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Invini> registrar(@Valid @RequestBody Invini invini) throws Exception {
		log.info("registrar...invini: " + invini);
		Invini obj = service.registrarInvini(invini);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdInvini()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PutMapping
	public ResponseEntity<Invini> modificar(@Valid @RequestBody Invini invini) throws Exception {
		log.info("modificar...invini: " + invini);
		Invini obj = service.registrarInvini(invini);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdInvini()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(invini), HttpStatus.OK);		
	}
	
	@PutMapping("/migrar")
	public ResponseEntity<Invini> migrar(
			@Valid @RequestBody Invini invini
			,
			@RequestParam(value="periodo", defaultValue = "0") Integer periodo
			) throws Exception {
		Invini obj = service.migrar(invini, 2023);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdInvini()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping(value = "/reportePdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE) //APPLICATION_PDF_VALUE		//APPLICATION_OCTET_STREAM_VALUE
	public ResponseEntity<byte[]> generarReporte(@PathVariable("id") Integer id) throws Exception {
//		logger.info("generarReporte...");
		byte[] data = null;
		data = service.reporteInventario(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Invini> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Invini obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Invini> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}
	
}
