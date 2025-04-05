package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Afamilia;

public interface IAfamiliaService extends ICRUD<Afamilia, Integer> {

	public String getNewFamilia(Integer idClase);
	public Afamilia registrarAfamilia(Afamilia afamilia) throws Exception;
	
	public Afamilia buscaPorFamilia(String familia, Integer idClase) throws Exception;
	Page<Afamilia> listarPageable(Integer idClase, String descri, Integer page, Integer size);
	List<Afamilia> listaPorClase(Integer idClase) throws Exception;
	
}