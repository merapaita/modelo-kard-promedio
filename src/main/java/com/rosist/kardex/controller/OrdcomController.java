package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.rosist.kardex.model.Ordcom;
import com.rosist.kardex.repo.IOrdcomRepo;
import com.rosist.kardex.service.IOrdcomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ordcom")
public class OrdcomController {

	@Autowired
	private IOrdcomService service;

	@Autowired
	private IOrdcomRepo repo;

	private static final Logger log = LoggerFactory.getLogger(OrdcomController.class);

	@GetMapping
	public ResponseEntity<List<Ordcom>> listar() throws Exception {
		return new ResponseEntity<>(service.listar(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Ordcom> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Ordcom obj = service.listarPorId(id);

		if (obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@GetMapping("/pageable")
	public ResponseEntity<Map<String, Object>> getAllOficina(
			@RequestParam(value = "periodo", defaultValue = "0") Integer periodo,
			@RequestParam(value = "numoc", defaultValue = "0") Integer numoc,
			@RequestParam(value = "estado", defaultValue = "") String estado,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) throws Exception {

		List<Ordcom> content;
		Integer totalReg = repo.countOrdcom(periodo, numoc, estado);
		content = service.listarOrdcom(periodo, numoc, estado, page, size);
		long totalPages = (size > 0 ? (totalReg - 1) / size + 1 : 0);

		Map<String, Object> response = new HashMap<>();
		response.put("content", content);
		response.put("number", page);
		response.put("totalElements", totalReg);
		response.put("totalPages", totalPages);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@GetMapping("/ruc/{ruc}")
//	public ResponseEntity<Ordcom> listarPorRuc(@PathVariable("ruc") String ruc) throws Exception{
//		Ordcom obj = service.listarPorId(id);
//		
//		if(obj == null) {
//			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
//		}
//		
//		return new ResponseEntity<>(obj, HttpStatus.OK);
//	}

	@PostMapping
	public ResponseEntity<Ordcom> registrar(@Valid @RequestBody Ordcom ordcom) throws Exception {
		log.info("registrar...ordcom: " + ordcom);
		Ordcom obj = service.registrarOrdcom(ordcom);
		// localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdOrdcom())
				.toUri();
		return ResponseEntity.created(location).build();

//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
	}

	@PutMapping
	public ResponseEntity<Ordcom> modificar(@Valid @RequestBody Ordcom ordcom) throws Exception {
		log.info("modificar...ordcom: " + ordcom);
		Ordcom obj = service.modificarOrdcom(ordcom);
		// localhost:8080/promotors/1
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdOrdcom())
				.toUri();
		return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(ordcom), HttpStatus.OK);		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/reportePdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE) // APPLICATION_PDF_VALUE
																						// //APPLICATION_OCTET_STREAM_VALUE
	public ResponseEntity<byte[]> generarReporte(@PathVariable("id") Integer id) throws Exception {
		log.info("generarReporte...");
		byte[] data = null;
		data = service.reporteOrdcom(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<Ordcom> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception {
		Ordcom obj = service.listarPorId(id);
		if (obj == null) {
			throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
		}
		EntityModel<Ordcom> recurso = EntityModel.of(obj);
		// localhost:8080/promotors/hateoas/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
		recurso.add(link1.withRel("parmae-recurso1"));
		recurso.add(link2.withRel("parmae-recurso2"));
		return recurso;
	}

}
