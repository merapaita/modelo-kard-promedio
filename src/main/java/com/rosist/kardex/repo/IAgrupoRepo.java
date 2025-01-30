package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Agrupo;

public interface IAgrupoRepo extends IGenericRepo<Agrupo, Integer> {
	
	@Query(value = "select lpad(ifnull(max(grupo),0)+1,2,'0') from agrupo where tipo=:tipo", nativeQuery = true)
	String getNewGrupo(@Param("tipo") String tipo);
	
	@Query(value = "SELECT * FROM agrupo WHERE tipo=:tipo", nativeQuery = true)
	List<Agrupo> listaPorTipo(@Param("tipo") String tipo);
	
	@Query(value = "SELECT * FROM agrupo WHERE tipo=:tipo", nativeQuery = true)
	Page<Agrupo> listaPorTipo(@Param("tipo") String tipo, Pageable page);
	
	@Query(value = "SELECT * FROM agrupo WHERE tipo=:tipo and descri like %:descri% ", nativeQuery = true)
	Page<Agrupo> listaPorDescri(@Param("tipo") String tipo, @Param("descri") String descri, Pageable page);

	@Query(value = "SELECT * FROM agrupo WHERE tipo=:tipo and grupo=:grupo", nativeQuery = true)
	Agrupo buscaPorGrupo(@Param("tipo") String tipo, @Param("grupo") String grupo);
	
}