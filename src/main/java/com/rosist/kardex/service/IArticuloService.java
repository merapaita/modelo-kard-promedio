package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Articulo;

public interface IArticuloService extends ICRUD<Articulo, Integer> {

	public Integer getNewId();
	public String getNewCodart(Articulo articulo);
	public Articulo registrarTransaccion(Articulo articulo) throws Exception;
	public Articulo modificarTransaccion(Articulo articulo) throws Exception;
	public List<Articulo> listarArticulo(String tipo, String nomart) throws Exception;
	
	Page<Articulo> listarPageable(String tipo, String nomart, Integer page, Integer size);
	List<Articulo> listaPorCodart(String tipo, String codart) throws Exception;
	List<Articulo> listaPorNomart(String tipo, String nomart) throws Exception;
	
}