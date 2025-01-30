package com.rosist.kardex.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.service.ICRUD;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

	protected abstract IGenericRepo<T, ID> getRepo();
	
	private static final Logger log = LoggerFactory.getLogger(CRUDImpl.class);

	@Override
	public T registrar(T t) throws Exception {
		return getRepo().save(t);
	}

	@Override
	public T modificar(T t) throws Exception {
		return getRepo().save(t);
	}

	@Override
	public List<T> listar() throws Exception {
		return getRepo().findAll();
	}

	@Override
	public T listarPorId(ID id) throws Exception {
		return getRepo().findById(id).orElse(null);
	}

	@Override
	public void eliminar(ID id) throws Exception {
		getRepo().deleteById(id);	
	}
	
}
