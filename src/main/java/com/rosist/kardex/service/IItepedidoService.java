package com.rosist.kardex.service;

import java.util.List;
import java.util.Map;

import com.rosist.kardex.dto.ItepedidoDto;

import com.rosist.kardex.model.Itepedido;

public interface IItepedidoService extends ICRUD<Itepedido, Integer> {

	List<Itepedido> listaPorPedido(int idPedido) throws Exception;
	public Map<String, Object> totales(Integer idPedido);
	public Itepedido registrarItepedido(ItepedidoDto itepedDto) throws Exception;
	public void eliminarItem(int idIteped) throws Exception;

}