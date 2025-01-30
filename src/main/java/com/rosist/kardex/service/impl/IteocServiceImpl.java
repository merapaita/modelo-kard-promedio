package com.rosist.kardex.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rosist.kardex.dto.IteocDto;
import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Iteoc;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Ordcom;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IIteocRepo;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.repo.IOrdcomRepo;
import com.rosist.kardex.service.IIteocService;
import com.rosist.kardex.service.IKardexService;
import com.rosist.kardex.service.IOrdcomService;

import jakarta.transaction.Transactional;

@Service
public class IteocServiceImpl extends CRUDImpl<Iteoc, Integer> implements IIteocService {

	@Autowired
	private IIteocRepo repo;

	@Autowired
	private IArticuloRepo repoArticulo;
	
	@Autowired
	private IOrdcomRepo repoOrdcom;
	
	@Autowired
	private IKardexRepo repoKardex;
	
	@Autowired
	private IKardexService srvKardex;

	@Autowired
	private IOrdcomService srvOrdcom;
	
	private static final Logger log = LoggerFactory.getLogger(IteocServiceImpl.class);

	@Override
	protected IGenericRepo<Iteoc, Integer> getRepo() {
		return repo;
	}

	@Transactional
	@Override
	public Iteoc registrarIteoc(IteocDto iteocDto) throws Exception {
		log.info("registrarIteoc grabando: ");
        String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		if (iteocDto.getArticulo().getIdArticulo()==null) {
			throw new ResourceNotFoundException("id Articulo no encontrado ");
		}
		Articulo articulo = repoArticulo.findById(iteocDto.getArticulo().getIdArticulo()).orElse(null);
		if (iteocDto.getOrdcom().getIdOrdcom()==null) {
			throw new ResourceNotFoundException("id Orden de compra no debe ser nulo ");
		}
		Ordcom ordcom = srvOrdcom.listarPorId(iteocDto.getOrdcom().getIdOrdcom());
		if (ordcom==null) {
			throw new ResourceNotFoundException("Orden de compra no encontrado ");
		}
		Iteoc iteoc = new Iteoc();
		iteoc.setTipkar(iteocDto.getTipkar());
		iteoc.setItem(repo.getNewItem(iteocDto.getOrdcom().getIdOrdcom()));
		iteoc.setArticulo(articulo);
		iteoc.setMenudeo(false);				// para eliminar en base de datos
		iteoc.setUnidad(iteocDto.getUnidad());
		iteoc.setUnimen(iteocDto.getUnimen());
		iteoc.setCantidad(iteocDto.getCantidad());
		iteoc.setFraccion(iteocDto.getFraccion());
		
		double cantidad = iteoc.getCantidad();
		double fraccion = iteoc.getFraccion();
		double fraccionArticulo = articulo.getFraccion();
		double totalCantidad = cantidad*fraccionArticulo + fraccion;
		
		if (totalCantidad==0.0) {
			throw new ResourceNotFoundException("Cantidades mal definidas ");
		}
		iteoc.setTotcan(totalCantidad);
		if (iteocDto.getPrecom()==0) {
			throw new ResourceNotFoundException("No se ha definido el precio de compra ");
		}
		iteoc.setPrecom(iteocDto.getPrecom());
		iteoc.setIgvcom(iteocDto.getIgvcom());
		iteoc.setPreuni(iteocDto.getPreuni());
		iteoc.setIgv(iteocDto.getIgv());
		iteoc.setValuni(iteocDto.getValuni());
		iteoc.setPreunifr(iteocDto.getPreunifr());
		iteoc.setIgvfr(iteocDto.getIgvfr());
		iteoc.setValunifr(iteocDto.getValunifr());
		iteoc.setEstado("00");
		iteoc.setOrdcom(ordcom);
		iteoc.setUserup(cUser);
		iteoc.setDuserup(dUser);
		repo.save(iteoc);

		Map<String, Object> totales = totales(ordcom.getIdOrdcom());
		Double totoc = (Double)totales.get("precom");
		Double igv = (Double)totales.get("igvcom");
		repoOrdcom.getInsertaTotales(totoc, igv, ordcom.getIdOrdcom());
		try {
			srvKardex.actualizaKardex(iteoc);		// , invini
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iteoc;
	}

	@Override
	public List<Iteoc> listaPorOrden(int idOrdcom) throws Exception {
		return repo.getListaxOrden(idOrdcom);
	}
	
	
	@Transactional
	@Override
	public void eliminarItem(int idIteoc) throws Exception {
		Iteoc iteoc = repo.findById(idIteoc).orElse(null);
		if (iteoc==null) {
			throw new ResourceNotFoundException("Item de Orden de compra no existe");
		}
		Kardex kardex = srvKardex.buscarPorItem("ORC", iteoc.getOrdcom().getPeriodo(), iteoc.getOrdcom().getNumoc(), idIteoc);
		if (kardex == null) {
			throw new ResourceNotFoundException("Item de Kardex no encontrado");
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
		repo.deleteById(idIteoc);

		Map<String, Object> totales = totales(iteoc.getOrdcom().getIdOrdcom());
		Double totoc = (Double)totales.get("precom");
		Double igv = (Double)totales.get("igvcom");
		repoOrdcom.getInsertaTotales(totoc, igv, iteoc.getOrdcom().getIdOrdcom());
		
	}
	
	@Override
	public Map<String, Object> totales(Integer idOrdcom) {
		Map<String, Object> resultado = new HashMap<>();
		List<Object[]> arrRpta;
		arrRpta = repo.getTotales(idOrdcom);
		
		arrRpta.forEach(x -> {
			resultado.put("precom", Double.parseDouble(String.valueOf(x[0])));
			resultado.put("igvcom", Double.parseDouble(String.valueOf(x[1])));
		});

		return resultado;
	}

}