package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Itepedido;

public interface IItepedidoRepo extends IGenericRepo<Itepedido, Integer> {

	@Query(value = "select * from itepedido where id_pedido=:idPedido", nativeQuery = true)
	List<Itepedido> getListaxPedido(@Param("idPedido") Integer idPedido);

	@Query(value = "SELECT SUM(monto) monto, SUM(igvmto) igvmto, SUM(valor) valor FROM itepedido WHERE id_pedido=:idPedido", nativeQuery = true)
	List<Object[]> getTotales(@Param("idPedido") Integer idPedido);
	
	@Query(value = "select ifnull(max(item),0)+1 from itepedido where id_pedido=:idPedido", nativeQuery = true)
	Integer getNewItem(@Param("idPedido") Integer idPedido);
	
	@Modifying
	@Query(value = "delete from itepedido where id_itepedido=:idItepedido", nativeQuery = true)
	void eliminaItem(@Param("idItepedido") Integer idItepedido);
	
}