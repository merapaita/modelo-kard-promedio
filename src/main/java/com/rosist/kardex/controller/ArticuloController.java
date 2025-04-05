package com.rosist.kardex.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.search.SearchAGrupoSpecification;
import com.rosist.kardex.search.SearchArticuloSpecification;
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
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.service.IArticuloService;

@RestController
@RequestMapping("/articulo")
public class ArticuloController {

    @Autowired
    private IArticuloService service;

    @Autowired
    private IArticuloRepo repo;

    private static final Logger logger = LoggerFactory.getLogger(ArticuloController.class);

    @GetMapping
    public ResponseEntity<List<Articulo>> listar(
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "nomart", defaultValue = "") String nomart
    ) throws Exception {
        SearchArticuloSpecification specification = new SearchArticuloSpecification(tipo, nomart);
        return new ResponseEntity<>(repo.findAll(specification), HttpStatus.OK);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<Articulo>> getAllArticulos(
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "nomart", defaultValue = "") String nomart,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) throws Exception {
        Page<Articulo> articulos = service.listarPageable(tipo, nomart, page, size);
        return new ResponseEntity<Page<Articulo>>(articulos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Articulo> listarPorId(@PathVariable("id") Integer id) throws Exception {
        Articulo obj = service.listarPorId(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/listarPorNombre")
    ///{tipo}/{nomart}
    public ResponseEntity<List<Articulo>> listarPorNombre(
            @RequestParam(value = "tipo", defaultValue = "B") String tipo,
            @RequestParam(value = "nomart", defaultValue = "") String nomart
    ) throws Exception {
        SearchArticuloSpecification specification = new SearchArticuloSpecification(tipo, nomart);
        List<Articulo> obj = repo.findAll(specification);
//        if (obj == null) {
//            throw new ModelNotFoundException("PATRON NO ENCONTRADO " + nomart);
//        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Articulo> registrar(@Valid @RequestBody Articulo articulo) throws Exception {
//		log.info("registrar...articulo: " + articulo);
        Articulo obj = service.registrarTransaccion(articulo);
        //localhost:8080/promotors/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdArticulo()).toUri();
        return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.registrar(promotor), HttpStatus.CREATED);	// 201
    }

    @PutMapping
    public ResponseEntity<Articulo> modificar(@Valid @RequestBody Articulo articulo) throws Exception {
//		log.info("modificar...articulo: " + articulo);
        Articulo obj = service.modificarTransaccion(articulo);
        //localhost:8080/promotors/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdArticulo()).toUri();
        return ResponseEntity.created(location).build();
//		return new ResponseEntity<>(service.modificar(articulo), HttpStatus.OK);		
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<Articulo> listarHateoasPorId(@PathVariable("id") Integer id) throws Exception {
        Articulo obj = service.listarPorId(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID NO ENCONTRADO " + id);
        }

        EntityModel<Articulo> recurso = EntityModel.of(obj);
        //localhost:8080/promotors/hateoas/1
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarHateoasPorId(id));
        recurso.add(link1.withRel("parmae-recurso1"));
        recurso.add(link2.withRel("parmae-recurso2"));

        return recurso;
    }
}
