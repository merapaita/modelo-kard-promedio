package com.rosist.kardex.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Invini;

public interface IInviniService extends ICRUD<Invini, Integer> {

	public Integer getNewInvini(Integer periodo);
	Page<Invini> listarPageable(Pageable page);
	public Invini registrarInvini(Invini invini) throws Exception;
	public byte[] reporteInventario(int id) throws Exception;
	public Invini migrar(Invini invini, Integer periodo) throws Exception;

}