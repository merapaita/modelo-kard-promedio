package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Pedido;

public interface IPedidoRepo extends IGenericRepo<Pedido, Integer> {

	@Query(value = "select ifnull(max(numped),0)+1 from pedido where periodo=:periodo", nativeQuery = true)
	Integer getNewPedido(@Param("periodo") Integer periodo);

	@Query(value = "select * from pedido where periodo=:periodo ORDER BY periodo, numped DESC", nativeQuery = true)
	Page<Pedido> listaPedido(@Param("periodo") Integer periodo, Pageable page);

	@Query(value = "call pr_pedido_count(:periodo, :numped, :estado) ", nativeQuery = true)
	Integer countPedido( 
			@Param("periodo") Integer periodo, 
			@Param("numped") Integer numped,
			@Param("estado") String estado)
	;
	
	@Query(value = "call pr_pedido(:periodo, :numped, :estado, :page, :size) ", nativeQuery = true)
	List<Object[]> listarPedido(
			@Param("periodo") Integer periodo,
			@Param("numped") Integer numped,
			@Param("estado") String estado,
			@Param("page") Integer page, @Param("size") Integer size );
	
	@Modifying
	@Query(value = "update pedido set totped=:totped, valped=:valped where id_pedido=:idPedido", nativeQuery = true)
	Integer getInsertaTotales(@Param("totped") Double totped, @Param("valped") Double valped,
			@Param("idPedido") Integer idPedido);

}