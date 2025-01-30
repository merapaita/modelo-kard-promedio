package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Pedido;

public interface IPedidoService extends ICRUD<Pedido, Integer> {

	public Integer getNewPedido(Integer periodo);
	public List<Pedido> listarPedido(Integer periodo, Integer numped, String estado, Integer page, Integer size) throws Exception;
	Page<Pedido> listarPageable(Pageable page);
	public Pedido registrarPedido(Pedido pedido) throws Exception;
	public Pedido modificarPedido(Pedido pedido) throws Exception;
	public byte[] reportePedido(int id) throws Exception;

}