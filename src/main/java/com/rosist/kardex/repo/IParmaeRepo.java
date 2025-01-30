package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Parmae;

public interface IParmaeRepo extends IGenericRepo<Parmae, Integer> {

	@Query(value = "SELECT * FROM parmae ORDER BY tipo, codigo", nativeQuery = true)
	List<Parmae> listar();
	
	@Query(value = "SELECT * FROM parmae ORDER BY tipo, codigo", nativeQuery = true)
	Page<Parmae> listar(Pageable page);
	
	@Query(value = "SELECT * FROM parmae WHERE tipo=:tipo", nativeQuery = true)
	List<Parmae> listarPorTipo(@Param("tipo") String tipo);
	
	@Query(value = "SELECT * FROM parmae WHERE tipo=:tipo", nativeQuery = true)
	Page<Parmae> listarPorTipo(@Param("tipo") String tipo, Pageable page);
	
	@Query(value = "SELECT * FROM parmae WHERE tipo=:tipo and codigo=:codigo", nativeQuery = true)
	Parmae buscaPorCodigo(@Param("tipo") String tipo, @Param("codigo") String codigo);
	
}
