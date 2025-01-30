package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Aclase;

public interface IAclaseRepo extends IGenericRepo<Aclase, Integer> {

	@Query(value = "select lpad(ifnull(max(clase),0)+1,2,'0') from aclase where id_grupo=:id_grupo", nativeQuery = true)
	String getNewClase(@Param("id_grupo") Integer id_grupo);
	
	@Query(value = "SELECT * FROM aclase WHERE id_grupo=:idGrupo", nativeQuery = true)
	List<Aclase> listaPorGrupo(@Param("idGrupo") Integer idGrupo);
	
	@Query(value = "SELECT * FROM aclase WHERE id_grupo=:idGrupo", nativeQuery = true)
	Page<Aclase> listaPorGrupo(@Param("idGrupo") Integer idGrupo, Pageable paging);

	@Query(value = "SELECT * FROM aclase WHERE id_grupo=:idGrupo and descri like %:descri% ", nativeQuery = true)
	Page<Aclase> listaPorDescri(@Param("idGrupo") Integer idGrupo, @Param("descri") String descri, Pageable page);
	
	@Query(value = "SELECT * FROM aclase WHERE clase=:clase and id_grupo=:id_grupo", nativeQuery = true)
	Aclase buscaPorClase(@Param("clase") String clase, @Param("id_grupo") Integer id_grupo);
	
}