package com.rosist.kardex.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Proveedor;

public interface IProveedorService extends ICRUD<Proveedor, Integer> {

	Page<Proveedor> listarPageable(Pageable page);
	Proveedor buscaPorRUC(String ruc) throws Exception;
	public Proveedor registrarTransaccion(Proveedor proveedor) throws Exception;
	public Proveedor modificaProveedor(Proveedor proveedor) throws Exception;
	
}