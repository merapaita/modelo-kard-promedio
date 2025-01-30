package com.rosist.kardex.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Invini;

public interface IInviniRepo extends IGenericRepo<Invini, Integer> {

	@Query(value = "select ifnull(max(numii),0)+1 from invini where periodo=:periodo", nativeQuery = true)
	Integer getNewInvini(@Param("periodo") Integer periodo);
	
	@Query(value = "select * from invini where periodo=:periodo", nativeQuery = true)
	Page<Invini> listaInv(@Param("periodo") Integer periodo, Pageable page);
	
	@Modifying
	@Query(value = "update invini set totii=:totii, totval=:totval where id_invini=:idInvini", nativeQuery = true)
	Integer getInsertaTotales(@Param("totii") Double totii,
			@Param("totval") Double totval, @Param("idInvini") Integer idInvini);

}