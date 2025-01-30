package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Ordcom;

public interface IOrdcomService extends ICRUD<Ordcom, Integer> {

	public Integer getNewOrdcom(Integer periodo);
	Page<Ordcom> listarPageable(Pageable page);
	public List<Ordcom> listarOrdcom(Integer periodo, Integer numoc, String estado, Integer page, Integer size) throws Exception;
	public Ordcom registrarOrdcom(Ordcom ordcom) throws Exception;
	public byte[] reporteOrdcom(int id) throws Exception;
	public Ordcom modificarOrdcom(Ordcom ordcom) throws Exception;

}