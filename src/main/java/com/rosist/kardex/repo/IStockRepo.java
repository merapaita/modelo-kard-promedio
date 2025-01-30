package com.rosist.kardex.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Stock;

public interface IStockRepo extends IGenericRepo<Stock, Integer> {

	@Query(value = "select s "
			+ "       from Stock s "
			+ "      where s.periodo=:periodo and s.tipkar=:tipkar and s.articulo.idArticulo=:id_articulo")
	Stock buscaPorArticulo(@Param("periodo") Integer periodo, 
			@Param("tipkar") String tipkar,
			@Param("id_articulo") Integer id_articulo);
	
}