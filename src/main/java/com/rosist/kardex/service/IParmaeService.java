package com.rosist.kardex.service;

import java.util.List;

import com.rosist.kardex.model.Parmae;

public interface IParmaeService extends ICRUD<Parmae, Integer> {

	List<Parmae> lista() throws Exception;
	List<Parmae> listaPorTipo(String tipo) throws Exception;
	Parmae buscaPorCodigo(String tipo, String Codigo) throws Exception;
	
}
