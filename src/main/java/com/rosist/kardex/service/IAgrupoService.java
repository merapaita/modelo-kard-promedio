package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.model.Proveedor;

public interface IAgrupoService extends ICRUD<Agrupo, Integer> {
	Page<Agrupo> listarPageable(String tipo, String grupo, Integer page, Integer size);
	Agrupo buscaPorGrupo(String tipo, String grupo) throws Exception;
	public String getNewGrupo(String tipo);
	public Agrupo registrarAgrupo(Agrupo agrupo) throws Exception;
}