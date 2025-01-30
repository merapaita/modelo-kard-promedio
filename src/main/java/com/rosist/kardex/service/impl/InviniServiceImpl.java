package com.rosist.kardex.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.model.Invini;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.model.Proveedor;
import com.rosist.kardex.model.Stock;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IInviniRepo;
import com.rosist.kardex.repo.IIteiiRepo;
import com.rosist.kardex.reportes.PDFInvini;
import com.rosist.kardex.service.IInviniService;
import com.rosist.kardex.service.IIteiiService;
import com.rosist.kardex.service.IKardexService;

import jakarta.transaction.Transactional;

@Service
public class InviniServiceImpl extends CRUDImpl<Invini, Integer> implements IInviniService {

	@Autowired
	private IInviniRepo repo;
	
	@Autowired
	private IIteiiRepo repoIteii;

//	@Autowired
//	private IIteiiService srvIteii;
	
	@Autowired
	private IKardexService srvKardex;

	private static final Logger log = LoggerFactory.getLogger(InviniServiceImpl.class);

	@Override
	protected IGenericRepo<Invini, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Invini> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public Integer getNewInvini(Integer periodo) {
		return repo.getNewInvini(periodo);
	}

	@Transactional
	@Override
	public Invini migrar(Invini invini, Integer periodo) throws Exception {
        String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		List<Stock> listaStock = srvKardex.listarStock(periodo, "", "", 0, -1, 0)
				.stream().filter(x -> x.getSaldoCantidad()>0.0)
				.collect(Collectors.toList());

		Invini _invini = repo.findById(invini.getIdInvini()).orElse(null);

		listaStock.forEach(reg -> {
			Iteii iteii = new Iteii();
			double precom = reg.getSaldoTotalCantidad() * reg.getPrecioPromedio();
			double igvCom = (precom - precom/1.18);
			double fraccion = reg.getArticulo().getFraccion();
			double igvfr = igvCom / fraccion;
			
			iteii.setInvini(_invini);
			iteii.setTipkar(String.valueOf(reg.getTipkar()));
			iteii.setArticulo(reg.getArticulo());
			iteii.setCantidad(reg.getSaldoCantidad());
			iteii.setUnidad("unidad");
			iteii.setFraccion(reg.getFraccion());
			iteii.setTotcan(reg.getSaldoTotalCantidad());
			iteii.setPreuni(reg.getPrecioPromedio());
			iteii.setIgv(reg.getIgvPromedio());
			iteii.setValuni(reg.getValorPromedio());
			iteii.setPreunifr(reg.getPrecioPromedioFraccion());
			iteii.setValunifr(reg.getValorPromedioFraccion());
			iteii.setUserup(cUser);
			iteii.setDuserup(dUser);
			iteii.setPrecom(precom);
			iteii.setIgvcom(igvCom);
			iteii.setIgvfr(igvfr);
			
			iteii = repoIteii.save(iteii);
			if (iteii!=null) {
				try {
					srvKardex.actualizaKardex(iteii);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Map<String, Object> totales = totales(invini.getIdInvini());
		Double totii = (Double)totales.get("precom");
		Double totval = (Double)totales.get("precom") - (Double)totales.get("igvcom");
		repo.getInsertaTotales(totii, totval, invini.getIdInvini());
		
		return _invini;
	}
	
//	@Override
	public Map<String, Object> totales(Integer idInvini) {
		Map<String, Object> resultado = new HashMap<>();
		List<Object[]> arrRpta;
		arrRpta = repoIteii.getTotales(idInvini);
		
		arrRpta.forEach(x -> {
			resultado.put("precom", Double.parseDouble(String.valueOf(x[0])));
			resultado.put("igvcom", Double.parseDouble(String.valueOf(x[1])));
		});

		return resultado;
	}

	
	@Transactional
	@Override
	public Invini registrarInvini(Invini invini) throws Exception {
		log.info("registrarInvini grabando: ");

		Proveedor proveedor = null;
		String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		if (invini.getNumii() == null) {
			invini.setNumii(getNewInvini(invini.getPeriodo()));
			invini.setEstado("00");
			invini.setUserup(cUser);
			invini.setDuserup(dUser);
		} else {
			invini.setUsercr(cUser);
			invini.setDusercr(dUser);
		}
		log.info("registrarInvini_impl_b...invini: ");
//		if (invini.getDetii()!=null) {
//			invini.getDetii().forEach(det -> {
//				det.setInvini(invini);
//				if (det.getIdIteii() == null) {
//					det.setUserup("cUser");
//					det.setDuserup(dUser);
//				} else {
//					det.setUsercr("cUser");
//					det.setDusercr(dUser);
//				}
//			});
//		}
		repo.save(invini);
//		if (invini.getDetii()!=null) {
//			invini.getDetii().forEach(det -> {
//				try {
//					log.info("registrarInvini...grabando kardex");
//					srvKardex.actualizaKardex(det);		// , invini
//					log.info("registrarInvini...grabe kardex");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//		}
		log.info("registrarTransaccion...grabada satisfactorio.-> ");
		return invini;
	}
	
	@Override
	public byte[] reporteInventario(int id) throws Exception {
		byte[] data = null;
//		logger.info("reporteAnalisis...id:" + id);
        Invini invini = repo.findById(id).orElse(null);
        List<Iteii> lIteii = null;
        if (invini!=null) {
        	lIteii = invini.getDetii();
        }
//        List<Iteii> lIteii = srvIteii.listaPorInventario(id);
//        logger.info("reporteAnalisis..." + analisis);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("invini", invini);
		parametros.put("lIteii", lIteii);
		
        PDFInvini pdfInvini = new PDFInvini(parametros);
        
		return pdfInvini.creaInvini();
	}
	

//	@Transactional
//	@Override
//	public Invini modificarInvini(Invini invini) throws Exception {
//		Proveedor proveedor = null;
//
//		String cUser = "Marco";
////        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
//		LocalDateTime dUser = LocalDateTime.now();
//
//		if (invini.getNumoc() == null) {
//			invini.setNumoc(getNewInvini(invini.getPeriodo()));
//			invini.setEstado("00");
//			invini.setUserup(cUser);
//			invini.setDuserup(dUser);
//		}
//		if (invini.getProveedor().getIdProveedor() == null) {
//			proveedor = repoProveedor.save(proveedor);
//			invini.setProveedor(invini.getProveedor());
//		}
//		invini.getDetinvini().forEach(det -> {
//			if (det.getIdIteoc() == null) {
//				det.setUserup("cUser");
//				det.setDuserup(dUser);
//			}
//		});
//
//		repo.save(invini);
//		log.info("registrarTransaccion...grabada.-> ");
//
//		return invini;
//
//		/*
//		 * for(DetalleConsulta det : consulta.getDetalleConsulta()) {
//		 * det.setConsulta(consulta); }
//		 */
//	}

}
