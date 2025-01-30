package com.rosist.kardex.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rosist.kardex.dto.IteiiDto;
import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Invini;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IInviniRepo;
import com.rosist.kardex.repo.IIteiiRepo;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.service.IInviniService;
import com.rosist.kardex.service.IIteiiService;
import com.rosist.kardex.service.IKardexService;

@Service
public class IteiiServiceImpl extends CRUDImpl<Iteii, Integer> implements IIteiiService {

	@Autowired
	private IIteiiRepo repo;

	@Autowired
	private IArticuloRepo repoArticulo;
	
	@Autowired
	private IInviniRepo repoInvini;
	
	@Autowired
	private IKardexRepo repoKardex;
	
	@Autowired
	private IKardexService srvKardex;

	@Autowired
	private IInviniService srvInvini;
	
	private static final Logger log = LoggerFactory.getLogger(IteiiServiceImpl.class);

	@Override
	protected IGenericRepo<Iteii, Integer> getRepo() {
		return repo;
	}

	@Transactional
	@Override
	public Iteii registrarIteii(IteiiDto iteiiDto) throws Exception {
		log.info("registrarInvini grabando: " + iteiiDto);
        String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		if (iteiiDto.getArticulo().getIdArticulo()==null) {
			throw new ResourceNotFoundException("id Articulo no encontrado ");
		}
		Articulo articulo = repoArticulo.findById(iteiiDto.getArticulo().getIdArticulo()).orElse(null);
		if (articulo==null) {
			throw new ResourceNotFoundException("Articulo no encontrado ");
		}
		if (iteiiDto.getInvini().getIdInvini()==null) {
			throw new ResourceNotFoundException("id Inventario no debe ser nulo ");
		}
		Invini invini = srvInvini.listarPorId(iteiiDto.getInvini().getIdInvini());
		if (invini==null) {
			throw new ResourceNotFoundException("Inventario inicial no encontrado ");
		}
		Iteii iteii = new Iteii();
		iteii.setTipkar(iteiiDto.getTipkar());
		iteii.setArticulo(articulo);
		iteii.setMenudeo(false);				// para eliminar en base de datos
		iteii.setUnidad(iteiiDto.getUnidad());
		iteii.setUnimen(iteiiDto.getUnimen());
		iteii.setCantidad(iteiiDto.getCantidad());
		iteii.setFraccion(iteiiDto.getFraccion());
		
		double cantidad = iteii.getCantidad();
		double fraccion = iteii.getFraccion();
		double fraccionArticulo = articulo.getFraccion();
		double totalCantidad = cantidad*fraccionArticulo + fraccion;
		
		if (totalCantidad==0.0) {
			throw new ResourceNotFoundException("Cantidades mal definidas ");
		}
		iteii.setTotcan(totalCantidad);
		if (iteiiDto.getPrecom()==0) {
			throw new ResourceNotFoundException("No se ha definido el precio de compra ");
		}
		iteii.setPrecom(iteiiDto.getPrecom());
		iteii.setIgvcom(iteiiDto.getIgvcom());
		iteii.setPreuni(iteiiDto.getPreuni());
		iteii.setIgv(iteiiDto.getIgv());
		iteii.setValuni(iteiiDto.getValuni());
		iteii.setPreunifr(iteiiDto.getPreunifr());
		iteii.setIgvfr(iteiiDto.getIgvfr());
		iteii.setValunifr(iteiiDto.getValunifr());
		
		iteii.setInvini(invini);
		
		iteii.setUserup(cUser);
		iteii.setDuserup(dUser);
		
		repo.save(iteii);
		
		Map<String, Object> totales = totales(invini.getIdInvini());
		Double totii = (Double)totales.get("precom");
		Double totval = (Double)totales.get("precom") - (Double)totales.get("igvcom");
		repoInvini.getInsertaTotales(totii, totval, invini.getIdInvini());
		
		try {
			srvKardex.actualizaKardex(iteii);		// , invini
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iteii;
	}

	@Override
	public List<Iteii> listaPorInventario(int idInvini) throws Exception {
		return repo.getListaxInventario(idInvini);
	}
	
	@Transactional
	@Override
	public void eliminarItem(int idIteii) throws Exception {
		Iteii iteii = repo.findById(idIteii).orElse(null);
		if (iteii==null) {
			throw new ResourceNotFoundException("Item de Inventario no existe");
		}
		Kardex kardex = srvKardex.buscarPorItem("INV", iteii.getInvini().getPeriodo(), iteii.getInvini().getNumii(), idIteii);
		if (kardex == null) {
			throw new ResourceNotFoundException("Item de Kardex no encontrado");
		}
		Integer periodo = kardex.getPeriodo();
		String tipo = kardex.getTipkar();
		Integer idArticulo = kardex.getArticulo().getIdArticulo();
		Integer correl = kardex.getCorrel();
		Integer ultimo = repoKardex.getUltimoCorrel(periodo, tipo, idArticulo);
		if (correl!=ultimo) {
			throw new ResourceNotFoundException("Solo se puede eliminar el ultimo item de la targeta. Por favor revise su kardex ");
		}
		srvKardex.eliminar(kardex.getIdKardex());
		srvKardex.recalcular(periodo, tipo, idArticulo);
		repo.deleteById(idIteii);
	}
	
	@SuppressWarnings("null")
	@Override
	public Map<String, Object> totales(Integer idInvini) {
		Map<String, Object> resultado = new HashMap<>();
		List<Object[]> arrRpta;
		arrRpta = repo.getTotales(idInvini);
		
		arrRpta.forEach(x -> {
			resultado.put("precom", Double.parseDouble(String.valueOf(x[0])));
			resultado.put("igvcom", Double.parseDouble(String.valueOf(x[1])));
		});

		return resultado;
	}

}
