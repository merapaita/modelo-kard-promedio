package com.rosist.kardex.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.rosist.kardex.model.Afamilia;
import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.search.SearchArticuloSpecification;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IAfamiliaRepo;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.service.IArticuloService;

@Service
public class ArticuloServiceImpl extends CRUDImpl<Articulo, Integer> implements IArticuloService {

	private static final Logger logger = LoggerFactory.getLogger(ArticuloServiceImpl.class);

	@Autowired
	private IArticuloRepo repo;

	@Autowired
	private IAfamiliaRepo repoFamilia;

	@Override
	protected IGenericRepo<Articulo, Integer> getRepo() {
		return repo;
	}

	private static final Logger log = LoggerFactory.getLogger(ArticuloServiceImpl.class);

	@Override
	public List<Articulo> listarArticulo(String tipo, String nomart) throws Exception {
		SearchArticuloSpecification specification = new SearchArticuloSpecification(tipo, nomart);
		return repo.findAll(specification);
	}

	@Override
	public Page<Articulo> listarPageable(String tipo, String nomart, Integer page, Integer size) {
		SearchArticuloSpecification specification = new SearchArticuloSpecification(tipo, nomart);
		List<Articulo> articulos = repo.findAll(specification);
		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), articulos.size());		//allCustomers.size()
		List<Articulo> pageContent = articulos.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, articulos.size());
	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
	}

	@Override
	public List<Articulo> listaPorCodart(String tipo, String codart) throws Exception {
		List<Articulo> articulo = repo.listaPorCodart(tipo, codart);
		return articulo;
	}

	@Override
	public List<Articulo> listaPorNomart(String tipo, String nomart) throws Exception {
		SearchArticuloSpecification specification = new SearchArticuloSpecification(tipo, nomart);
		List<Articulo> articulos = repo.findAll(specification);
//		List<Articulo> articulo = repo.listaPorNomart(tipo, nomart);
		return articulos;
	}

	@Override
	public Integer getNewId() {
		Integer id = repo.getNewIdArticulo();
		return id;
	}

	@Override
	public String getNewCodart(Articulo articulo) {
		String correl = repo.getNewCodart(articulo.getAfamilia().getIdFamilia());
		return articulo.getAfamilia().getAclase().getAgrupo().getGrupo() + articulo.getAfamilia().getAclase().getClase()
				+ articulo.getAfamilia().getFamilia() + correl;
	}

	@Transactional
	@Override
	public Articulo registrarTransaccion(Articulo articulo) throws Exception {
		if (articulo.getAfamilia().getIdFamilia()==null) {
			throw new ResourceNotFoundException("No se ha definido id de Familia.");
		}
		Afamilia familia = repoFamilia.findById(articulo.getAfamilia().getIdFamilia()).orElse(null);
		if (familia==null) {
			throw new ResourceNotFoundException("Familia definida no encontrada.");
		}
		articulo.setAfamilia(familia);
		articulo.setCodart(getNewCodart(articulo));

		if (articulo.getFraccion()>1) {
			articulo.setMenudeo(true);
			if (articulo.getUnimen()==null) {
				throw new ResourceNotFoundException("Unidad de menudeo mal definida.");
			}
		} else if(articulo.getFraccion()==1) {
			articulo.setMenudeo(false);
			if (articulo.getUnimen()!=null) {
				throw new ResourceNotFoundException("Unidad de menudeo mal definida.");
			}
		} else {
			throw new Exception("Menudeo Mal definido.");
		}
		repo.save(articulo);
		return articulo;
	}

	@Transactional
	@Override
	public Articulo modificarTransaccion(Articulo articulo) throws Exception {
	    Articulo _articulo = repo.findById(articulo.getIdArticulo()).orElse(null);

		_articulo.setNomart(articulo.getNomart());
		_articulo = repo.save(_articulo);
		return _articulo;
	}

}
