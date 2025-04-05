package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.search.SearchAClaseSpecification;
import com.rosist.kardex.search.SearchAGrupoSpecification;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.repo.IAclaseRepo;
import com.rosist.kardex.service.IAclaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/aclase")
public class AclaseController {

	@Autowired
	private IAclaseService service;
	
	@Autowired
	private IAclaseRepo repo;
	
	private static final Logger log = LoggerFactory.getLogger(AclaseController.class);

	@PreAuthorize("hasAuthority('READ_ALL_CLASS')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping
	public ResponseEntity<List<Aclase>> listar(
			@RequestParam(value="idGrupo", required = false) Integer idGrupo,
			@RequestParam(value="descri", required = false) String descri
	) throws Exception{
		SearchAClaseSpecification specification = new SearchAClaseSpecification(idGrupo, descri);
		return new ResponseEntity<>(repo.findAll(specification), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ALL_CLASS_FOR_GROUP')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/listaPorGrupo/{idGrupo}")
	public ResponseEntity<List<Aclase>> listaPorGrupo(@PathVariable("idGrupo") Integer idGrupo) throws Exception{
		SearchAClaseSpecification specification = new SearchAClaseSpecification(idGrupo, "");
		List<Aclase> obj = repo.findAll(specification);
//		if(obj == null) {
//			throw new ModelNotFoundException("GRUPO NO ENCONTRADO " + idGrupo);
//		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ALL_CLASS_PAGE')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/pageable")
	public ResponseEntity<Page<Aclase>> getAllClase(
			@RequestParam(value="idGrupo", required = false) Integer idGrupo,
			@RequestParam(value="descri", required = false) String descri,
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = "10") int size
			) {
		Page<Aclase> aclase = service.listarPageable(idGrupo, descri, page, size);
		return new ResponseEntity<Page<Aclase>>(aclase, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ONE_CLASS')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Aclase> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Aclase obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('CREATE_ONE_CLASS')")
//	@PreAuthorize("hasRole('ADMINISTRATOR'")
	@PostMapping
	public ResponseEntity<Aclase> registrar(@Valid @RequestBody Aclase aclase) throws Exception {
		Aclase obj = service.registrarAclase(aclase);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdClase()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PreAuthorize("hasAuthority('UPDATE_ONE_CLASS')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@PutMapping
	public ResponseEntity<Aclase> modificar(@Valid @RequestBody Aclase aclase) throws Exception {
		log.info("modificar...aclase: " + aclase);
		Aclase obj = service.registrar(aclase);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdClase()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(aclase), HttpStatus.OK);		
	}

	@PreAuthorize("hasAuthority('DELETE_ONE_CLASS')")
//	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Aclase> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Aclase obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		EntityModel<Aclase> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		
		return recurso;
		
	}
	
}