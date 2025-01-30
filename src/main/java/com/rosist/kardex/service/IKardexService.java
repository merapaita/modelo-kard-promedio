package com.rosist.kardex.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rosist.kardex.model.Iteoc;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Stock;

public interface IKardexService extends ICRUD<Kardex, Integer> {

	public Integer getNewCorrel(Integer periodo, String tipkar, Integer id_articulo);
	Page<Kardex> listarPageable(Pageable page);
	void actualizaKardex(Iteoc iteoc) throws Exception;
	void actualizaKardex(Iteii iteii) throws Exception;
	void actualizaKardex(Itepedido itepedido) throws Exception;
	void recalcular(Integer periodo, String tipkar, Integer idArticulo) throws Exception;
	List<Stock> listarStock(Integer periodo, String tipkar, String descri, Integer mes);
	Page<Stock> listarStock(Integer periodo, String tipkar, String descri, Integer mes, Integer page, Integer size);
	List<Kardex> listarKardex(Integer periodo, String tipkar, Integer idArticulo, Integer correl);
	Page<Kardex> listarKardex(Integer periodo, String tipkar, Integer idArticulo, Integer correl, Integer page, Integer size);
	
	List<Kardex> listaKardexPorStock(Integer periodo, String tipkar, Integer idArticulo, Integer correl);
	Page<Kardex> listaKardexPorStock(Integer periodo, String tipkar, Integer idArticulo, Integer correl, Integer page, Integer size);
	public Kardex buscarPorItem(String tipdoc, Integer periodo, Integer numdoc, Integer item) throws Exception;
	public byte[] reporteStock(int periodo, String tipkar, String descri, Integer mes) throws Exception;
	public byte[] reporteKardex(int periodo, String tipkar, Integer idArticulo, String descri) throws Exception;
	public byte[] existenciasMes(Integer periodo, Integer mes, String tipkar) throws Exception;
}