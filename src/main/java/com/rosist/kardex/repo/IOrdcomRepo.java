package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Ordcom;

public interface IOrdcomRepo extends IGenericRepo<Ordcom, Integer> {

	@Query(value = "select ifnull(max(numoc),0)+1 from ordcom where periodo=:periodo", nativeQuery = true)
	Integer getNewOrdcom(@Param("periodo") Integer periodo);

	@Query(value = "call pr_ordcom_count(:periodo, :numoc, :estado) ", nativeQuery = true)
	Integer countOrdcom( 
			@Param("periodo") Integer periodo, 
			@Param("numoc") Integer numoc,
			@Param("estado") String estado
			);
	
	@Query(value = "call pr_ordcom(:periodo, :numoc, :estado, :page, :size) ", nativeQuery = true)
	List<Object[]> listaOrdcom(
			@Param("periodo") Integer periodo, 
			@Param("numoc") Integer numoc, 
			@Param("estado") String estado, 
			@Param("page") Integer page, @Param("size") Integer size );
	
	@Modifying
	@Query(value = "update ordcom set totoc=:totoc, igv=:igvcom where id_ordcom=:idOrdcom", nativeQuery = true)
	Integer getInsertaTotales(@Param("totoc") Double totoc,
			@Param("igvcom") Double igvcom, @Param("idOrdcom") Integer idOrdcom);

}
