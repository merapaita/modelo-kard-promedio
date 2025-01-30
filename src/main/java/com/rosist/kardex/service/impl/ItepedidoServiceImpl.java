package com.rosist.kardex.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rosist.kardex.dto.ItepedidoDto;
import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Pedido;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IItepedidoRepo;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.repo.IPedidoRepo;
import com.rosist.kardex.service.IItepedidoService;
import com.rosist.kardex.service.IKardexService;
import com.rosist.kardex.service.IPedidoService;

import jakarta.transaction.Transactional;

@Service
public class ItepedidoServiceImpl extends CRUDImpl<Itepedido, Integer> implements IItepedidoService {

	@Autowired
	private IItepedidoRepo repo;
	
	@Autowired
	private IPedidoRepo repoPedido;
	
	@Autowired
	private IArticuloRepo repoArticulo;
	
	@Autowired
	private IKardexRepo repoKardex;
	
	@Autowired
	private IKardexService srvKardex;

	@Autowired
	private IPedidoService srvPedido;
	
	private static final Logger log = LoggerFactory.getLogger(ItepedidoServiceImpl.class);

	@Override
	protected IGenericRepo<Itepedido, Integer> getRepo() {
		return repo;
	}

	@Transactional
	@Override
	public Itepedido registrarItepedido(ItepedidoDto itepedidoDto) throws Exception {
        String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		if (itepedidoDto.getArticulo().getIdArticulo()==null) {
			throw new ResourceNotFoundException("id Articulo no encontrado ");
		}
		Articulo articulo = repoArticulo.findById(itepedidoDto.getArticulo().getIdArticulo()).orElse(null);
		
		if (itepedidoDto.getPedido().getIdPedido()==null) {
			throw new ResourceNotFoundException("id Pedido no debe ser nulo ");
		}
		Pedido pedido = srvPedido.listarPorId(itepedidoDto.getPedido().getIdPedido());
		if (pedido==null) {
			throw new ResourceNotFoundException("Pecosa no encontrado ");
		}
		Itepedido itepedido = new Itepedido();
		itepedido.setTipkar(itepedidoDto.getTipkar());
		itepedido.setItem(repo.getNewItem(itepedidoDto.getPedido().getIdPedido()));
		itepedido.setArticulo(articulo);
		itepedido.setCorrel(itepedidoDto.getCorrel());
		
		itepedido.setMenudeo(false);				  // para eliminar en base de datos
		itepedido.setUnidad(itepedidoDto.getUnidad());  // para eliminar en base de datos
		itepedido.setUnimen(itepedidoDto.getUnimen());  // para eliminar en base de datos
		itepedido.setCantidad(itepedidoDto.getCantidad());
		itepedido.setFraccion(itepedidoDto.getFraccion());
		
		double cantidad = itepedido.getCantidad();
		double fraccion = itepedido.getFraccion();
		double fraccionArticulo = articulo.getFraccion();
		double totalCantidad = cantidad*fraccionArticulo + fraccion;
		
		if (totalCantidad==0.0) {
			throw new ResourceNotFoundException("Cantidades mal definidas ");
		}
		itepedido.setTotcan(totalCantidad);
		if (itepedidoDto.getValor()==0.0) {
			throw new ResourceNotFoundException("Valor mal definido mal definidas ");
		}
		itepedido.setValor(itepedidoDto.getValor());
		itepedido.setValuni(itepedidoDto.getValuni());
		itepedido.setValunifr(itepedidoDto.getValunifr());

		itepedido.setPedido(pedido);
		itepedido.setEstado("00");
		itepedido.setUserup(cUser);
		itepedido.setDuserup(dUser);
		
		repo.save(itepedido);
		
		Map<String, Object> totales = totales(pedido.getIdPedido());
//		Double totpecosa   = (Double)totales.get("monto");
		Double valpec      = (Double)totales.get("valor");
		repoPedido.getInsertaTotales(0.0, valpec, pedido.getIdPedido());
		
		try {
			srvKardex.actualizaKardex(itepedido);		// , invini
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itepedido;
	}

	@Override
	public List<Itepedido> listaPorPedido(int idPedido) throws Exception {
		return repo.getListaxPedido(idPedido);
	}
	
	@Transactional
	@Override
	public void eliminarItem(int idItepedido) throws Exception {
		Itepedido itepedido = repo.findById(idItepedido).orElse(null);
		if (itepedido==null) {
			throw new ResourceNotFoundException("Item de Pedido no existe");
		}
		Kardex kardex = srvKardex.buscarPorItem("PEC", itepedido.getPedido().getPeriodo(), itepedido.getPedido().getNumped(), idItepedido);
		if (kardex == null) {
			throw new ResourceNotFoundException("Valor mal definido mal definidas ");
		}
		Integer periodo = kardex.getPeriodo();
		String  tipo = kardex.getTipkar();
		Integer idArticulo = kardex.getArticulo().getIdArticulo();
		Integer correl = kardex.getCorrel();
		Integer ultimo = repoKardex.getUltimoCorrel(periodo, tipo, idArticulo);
		if (correl!=ultimo) {
			throw new ResourceNotFoundException("Solo se puede eliminar el ultimo item de la targeta. Por favor revise su kardex ");
		}
		srvKardex.eliminar(kardex.getIdKardex());
		srvKardex.recalcular(periodo, tipo, idArticulo);
		repo.delete(itepedido);
	}

	@Override
	public Map<String, Object> totales(Integer idPecosa) {
		Map<String, Object> resultado = new HashMap<>();
		List<Object[]> arrRpta;
		arrRpta = repo.getTotales(idPecosa);
		
		arrRpta.forEach(x -> {
//			resultado.put("monto", Double.parseDouble(String.valueOf(x[0])));
			resultado.put("valor", Double.parseDouble(String.valueOf(x[2])));
		});

		return resultado;
	}
	
}