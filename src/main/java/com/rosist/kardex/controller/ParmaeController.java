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
import com.rosist.kardex.model.Parmae;
import com.rosist.kardex.repo.IParmaeRepo;
import com.rosist.kardex.service.IParmaeService;

@RestController
@RequestMapping("/parmae")
public class ParmaeController {

	@Autowired
	private IParmaeService service;
	
	@Autowired
	private IParmaeRepo repo;
	
	private static final Logger logger = LoggerFactory.getLogger(ParmaeController.class);
	
	@GetMapping
	public ResponseEntity<List<Parmae>> listar() throws Exception{
		return new ResponseEntity<>(service.lista(), HttpStatus.OK);
	}

	@GetMapping("/pageable")
	public ResponseEntity<Map<String, Object>> getAllParametros(
	        @RequestParam(value="tipo", required = false) String tipo,
	        @RequestParam(value="page", defaultValue = "0") int page,
	        @RequestParam(value="size", defaultValue = "10") int size
	      ) {

	      logger.info("getAllParametros...tipo:" + tipo);
	      logger.info("getAllParametros...page:" + page);
	      logger.info("getAllParametros...size:" + size);
	      
	      List<Parmae> parmae = new ArrayList<Parmae>();
	      Pageable paging = PageRequest.of(page, size);
	      
	      Page<Parmae> pageContent = null;
	      if (tipo == null) 
	    	  pageContent = repo.listar(paging);
   	      else 
	    	  pageContent = repo.listarPorTipo(tipo, paging);
	      
	      parmae = pageContent.getContent();
	      Map<String, Object> response = new HashMap<>();
//	      response.put("empleados", empleados);
//	      response.put("currentPage", pageTuts.getNumber());
//	      response.put("totalItems", pageTuts.getTotalElements());
//	      response.put("totalPages", pageTuts.getTotalPages());
	      response.put("content", parmae);
	      logger.info("getAllParametros...parmae:" + parmae);
	      response.put("number", pageContent.getNumber());
	      response.put("totalElements", pageContent.getTotalElements());
	      response.put("totalPages", pageContent.getTotalPages());

	      return new ResponseEntity<>(response, HttpStatus.OK);
	  }

	@GetMapping("/{id}")
	public ResponseEntity<Parmae> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Parmae obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<Parmae>> listarPorTipo(@PathVariable("tipo") String tipo) throws Exception{
		List<Parmae> obj = service.listaPorTipo(tipo);
		if(obj == null) {
			throw new ModelNotFoundException("TIPO " + tipo + " NO ENCONTRADO ");
		}
		
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@GetMapping("codigo/{tipo}/{codigo}")
	public ResponseEntity<Parmae> buscaPorCodigo(@PathVariable("tipo") String tipo, @PathVariable("codigo") String codigo) throws Exception{
		Parmae obj = service.buscaPorCodigo(tipo, codigo);
		
		if(obj == null) {
			throw new ModelNotFoundException("CODIGO NO ENCONTRADO " + codigo);
		}
		
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Parmae> registrar(@Valid @RequestBody Parmae parmae) throws Exception {
		Parmae obj = service.registrar(parmae);
		//localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdParmae()).toUri();
		return ResponseEntity.created(location).build();
		
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PutMapping
	public ResponseEntity<Parmae> modificar(@Valid @RequestBody Parmae parmae) throws Exception {
		return new ResponseEntity<>(service.modificar(parmae), HttpStatus.OK);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<Parmae> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception{
		Parmae obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		EntityModel<Parmae> recurso = EntityModel.of(obj);
		//localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		
		return recurso;
		
	}
	
}
