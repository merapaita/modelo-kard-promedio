package com.rosist.kardex.service.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Afamilia;
import com.rosist.kardex.model.Agrupo;
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
	public List<Articulo> listarArticulo(String tipo, String nomart, Integer page, Integer size) throws Exception {
		List<Articulo> articulos = new ArrayList<>();
		List<Object[]> registros = repo.listaArticulo(tipo, nomart, page, size);
		registros.forEach(reg -> {
			Agrupo grupo = new Agrupo();
			grupo.setIdGrupo(Integer.parseInt(String.valueOf(reg[9])));
			grupo.setTipo(String.valueOf(reg[10]));
			grupo.setGrupo(String.valueOf(reg[11]));
			grupo.setDescri(String.valueOf(reg[12]));

			Aclase clase = new Aclase();
			clase.setIdClase(Integer.parseInt(String.valueOf(reg[6])));
			clase.setClase(String.valueOf(reg[7]));
			clase.setDescri(String.valueOf(String.valueOf(reg[8])));
			clase.setAgrupo(grupo);

			Afamilia familia = new Afamilia();
			familia.setIdFamilia(Integer.parseInt(String.valueOf(reg[3])));
			familia.setFamilia(String.valueOf(reg[4]));
			familia.setDescri(String.valueOf(reg[5]));
			familia.setAclase(clase);

			Articulo articulo = new Articulo();
			articulo.setIdArticulo(Integer.parseInt(String.valueOf(reg[0])));
			articulo.setTipo(String.valueOf(reg[1]));
			articulo.setCodart(String.valueOf(reg[2]));
			articulo.setAfamilia(familia);

			articulo.setNomart(String.valueOf(reg[13]));
			articulo.setMenudeo(Boolean.parseBoolean(String.valueOf(reg[14])));
			articulo.setFraccion(Integer.parseInt(String.valueOf(reg[15])));
			articulo.setUnidad(String.valueOf(reg[16]));
			articulo.setUnimen(String.valueOf(reg[17]));

			articulos.add(articulo);
		});

		return articulos;
	}

	@Override
	public Page<Articulo> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public List<Articulo> listaPorCodart(String tipo, String codart) throws Exception {
		List<Articulo> articulo = repo.listaPorCodart(tipo, codart);
		return articulo;
	}

	@Override
	public List<Articulo> listaPorNomart(String tipo, String nomart) throws Exception {
		logger.info("listaPorNomart...tipo:" + tipo + " nomart:" + nomart);
		List<Articulo> articulo = repo.listaPorNomart(tipo, nomart);
		return articulo;
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
