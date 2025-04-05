package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Agrupo;

public interface IAclaseService extends ICRUD<Aclase, Integer> {

	Page<Aclase> listarPageable(Integer idGrupo, String descri, Integer page, Integer size);
	List<Aclase> listaPorGrupo(Integer idGrupo) throws Exception;
	Aclase buscaPorClase(String clase, Integer id_grupo) throws Exception;
	
	public String getNewClase(Integer idGrupo);
	public Aclase registrarAclase(Aclase aclase) throws Exception;
}