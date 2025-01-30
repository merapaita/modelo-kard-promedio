package com.rosist.kardex.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.model.Empleado;
import com.rosist.kardex.repo.IEmpleadoRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.service.IEmpleadoService;

@Service
public class EmpleadoServiceImpl extends CRUDImpl<Empleado, Integer> implements IEmpleadoService {

	@Autowired
	private IEmpleadoRepo repo;

	private static final Logger log = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

	@Override
	protected IGenericRepo<Empleado, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Empleado> listarPageable(Pageable page) {
		return repo.findAll(page);
	}
	
}
