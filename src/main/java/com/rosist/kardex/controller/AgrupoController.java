package com.rosist.kardex.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rosist.kardex.search.SearchAGrupoSpecification;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.repo.IAgrupoRepo;
import com.rosist.kardex.service.IAgrupoService;
import com.rosist.kardex.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/agrupo")
public class AgrupoController {

	@Autowired
	private IAgrupoService service;
	@Autowired
	private IAgrupoRepo repo;
	
	private static final Logger log = LoggerFactory.getLogger(AgrupoController.class);

	@PreAuthorize("hasAuthority('READ_ALL_GROUPS')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping
	public ResponseEntity<List<Agrupo>> listar(
			@RequestParam(value="tipo", required = false) String tipo,
			@RequestParam(value="descri", required = false) String descri
	) throws Exception{
		SearchAGrupoSpecification specification = new SearchAGrupoSpecification(tipo, descri);
		return new ResponseEntity<>(repo.findAll(specification), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ALL_GROUP_PAGE')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/pageable")
	  public ResponseEntity<Page<Agrupo>> getAllAgrupo(
		        @RequestParam(value="tipo", required = false) String tipo,
		        @RequestParam(value="descri", required = false) String descri,
		        @RequestParam(value="page", defaultValue = "0") int page,
		        @RequestParam(value="size", defaultValue = "10") int size
			  ) {
		Page<Agrupo> agrupo = service.listarPageable(tipo, descri, page, size);
		return new ResponseEntity<Page<Agrupo>>(agrupo, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ONE_GROUP')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Agrupo> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Agrupo obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_ALL_GROUP_FOR_TYPE')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@GetMapping("/listaPorTipo/{tipo}")
	public ResponseEntity<List<Agrupo>> listaPorTipo(@PathVariable("tipo") String tipo) throws Exception{
		SearchAGrupoSpecification specification = new SearchAGrupoSpecification(tipo, "");
		List<Agrupo> obj = repo.findAll(specification);
		if(obj == null) {
			throw new ModelNotFoundException("TIPO NO ENCONTRADO ");
		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('CREATE_ONE_GROUP')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@PostMapping
	public ResponseEntity<Agrupo> registrar(@Valid @RequestBody Agrupo agrupo) throws Exception {
		String tipo = agrupo.getTipo();
		String grupo = agrupo.getGrupo();
		Agrupo obj = service.buscaPorGrupo(tipo, grupo);
		if(obj != null) {
			throw new ResourceNotFoundException("Grupo ya existe...");
		}
		obj = service.registrarAgrupo(agrupo);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdGrupo()).toUri();
		return ResponseEntity.created(location).build();
		
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PreAuthorize("hasAuthority('UPDATE_ONE_GROUP')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@PutMapping
	public ResponseEntity<Agrupo> modificar(@Valid @RequestBody Agrupo agrupo) throws Exception {
		log.info("modificar...agrupo: " + agrupo);
		Agrupo obj = service.registrar(agrupo);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdGrupo()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(agrupo), HttpStatus.OK);		
	}

	@PreAuthorize("hasAuthority('DELETE_ONE_GROUP')")
//	@PreAuthorize("hasAnyRole('ADMINISTRATOR','ASSISTANT_ADMINISTRATOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
//	@GetMapping("/hateoas/{id}")
//	public EntityModel<Agrupo> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
//		Agrupo obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		EntityModel<Agrupo> recurso = EntityModel.of(obj);
//		//localhost:8080/promotors/hateoas/1
//		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
//		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
//		recurso.add(link1.withRel("parmae-recurso1"));
//		recurso.add(link2.withRel("parmae-recurso2"));
//		
//		return recurso;
//		
//	}
	
}
