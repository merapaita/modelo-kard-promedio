package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Afamilia;

public interface IAfamiliaRepo extends IGenericRepo<Afamilia, Integer> {

	@Query(value = "select lpad(ifnull(max(familia),0)+1,4,'0') from afamilia where id_clase=:id_clase", nativeQuery = true)
	String getNewFamilia(@Param("id_clase") Integer id_clase);

	@Query(value = "SELECT * FROM afamilia WHERE id_clase=:idClase", nativeQuery = true)
	List<Afamilia> listaPorClase(@Param("idClase") Integer idClase);
	
	@Query(value = "SELECT * FROM afamilia WHERE id_clase=:idClase", nativeQuery = true)
	Page<Afamilia> listaPorClase(@Param("idClase") Integer idClase, Pageable page);
	
	@Query(value = "SELECT * FROM afamilia WHERE id_clase=:idClase and descri like%:descri%", nativeQuery = true)
	Page<Afamilia> listaPorDescri(@Param("idClase") Integer idClase, @Param("descri") String descri, Pageable page);

	@Query(value = "SELECT * FROM afamilia WHERE familia=:familia and id_clase=:id_clase", nativeQuery = true)
	Afamilia buscaPorFamilia(@Param("familia") String clase, @Param("id_clase") Integer id_clase);
}