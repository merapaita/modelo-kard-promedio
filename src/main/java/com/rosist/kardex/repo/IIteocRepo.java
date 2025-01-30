package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Iteoc;

public interface IIteocRepo extends IGenericRepo<Iteoc, Integer> {

	@Query(value = "select * from iteoc where id_ordcom=:idOrdcom", nativeQuery = true)
	List<Iteoc> getListaxOrden(@Param("idOrdcom") Integer idOrdcom);

	@Query(value = "SELECT SUM(precom) precom, SUM(igvcom) igvcom FROM iteoc WHERE id_ordcom=:idOrdcom", nativeQuery = true)
	List<Object[]> getTotales(@Param("idOrdcom") Integer idOrdcom);

	@Query(value = "select ifnull(max(item),0)+1 from iteoc where id_ordcom=:idOrdcom", nativeQuery = true)
	Integer getNewItem(@Param("idOrdcom") Integer idOrdcom);

}