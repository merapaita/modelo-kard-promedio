package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Iteii;

public interface IIteiiRepo extends IGenericRepo<Iteii, Integer> {

	@Query(value = "select * from iteii where id_invini=:idInvini", nativeQuery = true)
	List<Iteii> getListaxInventario(@Param("idInvini") Integer idInvini);

	@Query(value = "SELECT SUM(precom) precom, SUM(igvcom) igvcom FROM iteii WHERE id_invini=:idInvini", nativeQuery = true)
	List<Object[]> getTotales(@Param("idInvini") Integer idInvini);
	
}