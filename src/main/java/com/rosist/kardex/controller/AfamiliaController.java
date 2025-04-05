package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.search.SearchAClaseSpecification;
import com.rosist.kardex.search.SearchAFamiliaSpecification;
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
import com.rosist.kardex.model.Afamilia;
import com.rosist.kardex.repo.IAfamiliaRepo;
import com.rosist.kardex.service.IAfamiliaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/afamilia")
public class AfamiliaController {

	@Autowired
	private IAfamiliaService service;
	
	@Autowired
	private IAfamiliaRepo repo;
	
	private static final Logger log = LoggerFactory.getLogger(AfamiliaController.class);

	@GetMapping
	public ResponseEntity<List<Afamilia>> listar(
			@RequestParam(value="idClase", required = false) Integer idClase,
			@RequestParam(value="descri", required = false) String descri
	) throws Exception{
		SearchAFamiliaSpecification specification = new SearchAFamiliaSpecification(idClase, descri);
		return new ResponseEntity<>(repo.findAll(specification), HttpStatus.OK);
	}

	@GetMapping("/pageable")
	public ResponseEntity<Page<Afamilia>> getAllClase(
			@RequestParam(value="idClase", required = false) Integer idClase,
			@RequestParam(value="descri", required = false) String descri,
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = "10") int size
	) {
		Page<Afamilia> afamilia = service.listarPageable(idClase, descri, page, size);
		return new ResponseEntity<Page<Afamilia>>(afamilia, HttpStatus.OK);
	}

//	@GetMapping("/pageable")
//	public ResponseEntity<Map<String, Object>> getAllFamilia(
//			@RequestParam(value="idClase", required = false) Integer idClase,
//			@RequestParam(value="descri", required = false) String descri,
//			@RequestParam(value="page", defaultValue = "0") int page,
//			@RequestParam(value="size", defaultValue = "10") int size
//			) {
//
//		List<Afamilia> familia = new ArrayList<Afamilia>();
//		Pageable paging = PageRequest.of(page, size);
//
//		Page<Afamilia> pageTuts;
//		if (idClase == null) throw new ModelNotFoundException("CLASE NO ENCONTRADA " + idClase);
//		if (descri != null) {
//			pageTuts = repo.listaPorDescri(idClase, descri, paging);
//		}
//		else {
//			pageTuts = repo.listaPorClase(idClase, paging);
//		}
//
//		familia = pageTuts.getContent();
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("content", familia);
//		response.put("number", pageTuts.getNumber());
//		response.put("totalElements", pageTuts.getTotalElements());
//		response.put("totalPages", pageTuts.getTotalPages());
//
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	@GetMapping("/listaPorClase/{idClase}")
	public ResponseEntity<List<Afamilia>> listaPorCloase(@PathVariable("idClase") Integer idClase) throws Exception{

		SearchAFamiliaSpecification specification = new SearchAFamiliaSpecification(idClase, "");
		List<Afamilia> obj = repo.findAll(specification);
//		if(obj == null) {
//			throw new ModelNotFoundException("CLASE NO ENCONTRADO " + idClase);
//		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Afamilia> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Afamilia obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Afamilia> registrar(@Valid @RequestBody Afamilia afamilia) throws Exception {
		Afamilia obj = service.registrarAfamilia(afamilia);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdFamilia()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PutMapping
	public ResponseEntity<Afamilia> modificar(@Valid @RequestBody Afamilia afamilia) throws Exception {
		Afamilia obj = service.registrar(afamilia);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdFamilia()).toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(afamilia), HttpStatus.OK);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Afamilia> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Afamilia obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Afamilia> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}
}