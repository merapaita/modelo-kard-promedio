package com.rosist.kardex.service;

import java.util.List;
import java.util.Map;

import com.rosist.kardex.dto.IteiiDto;

//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Iteii;

public interface IIteiiService extends ICRUD<Iteii, Integer> {

	List<Iteii> listaPorInventario(int idInvini) throws Exception;
	public Map<String, Object> totales(Integer id_invini);
	public Iteii registrarIteii(IteiiDto iteii) throws Exception;
	public void eliminarItem(int idIteii) throws Exception;

}