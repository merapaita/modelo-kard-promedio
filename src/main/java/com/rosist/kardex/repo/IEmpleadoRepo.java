package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.rosist.kardex.model.Empleado;

public interface IEmpleadoRepo extends IGenericRepo<Empleado, Integer> {

	@Query(value = "SELECT * "
			+ "	 from empleado "
			+ "	     where nombre like %:nombre% ", nativeQuery = true)
	List<Empleado> findByNombre(String nombre);
	
	@Query(value = "SELECT * "
			+ "	 from empleado "
			+ "	     where nombre like %:nombre% ", nativeQuery = true)
	Page<Empleado> findByNombre(String nombre, Pageable pageable);

}