package com.rosist.kardex.service;

import java.util.List;
import java.util.Map;

import com.rosist.kardex.dto.IteocDto;
import com.rosist.kardex.model.Articulo;

//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Iteoc;

public interface IIteocService extends ICRUD<Iteoc, Integer> {

	List<Iteoc> listaPorOrden(int idOrdcom) throws Exception;
	public Map<String, Object> totales(Integer id_ordcom);
	public Iteoc registrarIteoc(IteocDto iteoc) throws Exception;
	public void eliminarItem(int idIteoc) throws Exception;

}