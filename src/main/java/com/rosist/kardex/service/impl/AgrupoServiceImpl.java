package com.rosist.kardex.service.impl;

import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.repo.IAgrupoRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.service.IAgrupoService;

@Service
public class AgrupoServiceImpl extends CRUDImpl<Agrupo, Integer> implements IAgrupoService {

	private static final Logger log = LoggerFactory.getLogger(AgrupoServiceImpl.class);
	
	@Autowired
	private IAgrupoRepo repo;
	
	@Override
	protected IGenericRepo<Agrupo, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Agrupo> listarPageable(Pageable page) {
		return repo.findAll(page);
	}
	
	@Override
	public List<Agrupo> listaPorTipo(String tipo) throws Exception {
		List<Agrupo> artmaegrupo = repo.listaPorTipo(tipo);
		return artmaegrupo;
	}

	@Override
	public Agrupo buscaPorGrupo(String tipo, String grupo) throws Exception {
		Agrupo agrupo = repo.buscaPorGrupo(tipo, grupo);
		return agrupo;
	}
	
	@Override
	public String getNewGrupo(String tipo) {
		String newGrupo = repo.getNewGrupo(tipo);
		return newGrupo;
	}
	
	@Transactional
	@Override
	public Agrupo registrarAgrupo(Agrupo agrupo) throws Exception {
		repo.save(agrupo);
		return agrupo;
	}
	
}