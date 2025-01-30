package com.rosist.kardex.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Empleado;

public interface IEmpleadoService extends ICRUD<Empleado, Integer> {

	Page<Empleado> listarPageable(Pageable page);

}