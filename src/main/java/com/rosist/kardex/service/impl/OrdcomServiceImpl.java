package com.rosist.kardex.service.impl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Iteoc;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Ordcom;
import com.rosist.kardex.model.Pedido;
import com.rosist.kardex.model.Proveedor;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IIteocRepo;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.repo.IOrdcomRepo;
import com.rosist.kardex.repo.IProveedorRepo;
import com.rosist.kardex.reportes.PDFOrdcom;
import com.rosist.kardex.service.IIteocService;
import com.rosist.kardex.service.IKardexService;
import com.rosist.kardex.service.IOrdcomService;

@Service
public class OrdcomServiceImpl extends CRUDImpl<Ordcom, Integer> implements IOrdcomService {

	@Autowired
	private IOrdcomRepo repo;

//	@Autowired
//	private IIteocService srvIteoc;

	@Autowired
	private IKardexRepo repoKardex;

	@Autowired
	private IKardexService srvKardex;

	@Autowired
	private IProveedorRepo repoProveedor;

	@Autowired
	private IIteocRepo repoIteoc;

	private static final Logger log = LoggerFactory.getLogger(OrdcomServiceImpl.class);

	@Override
	protected IGenericRepo<Ordcom, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Ordcom> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public List<Ordcom> listarOrdcom(Integer periodo, Integer numoc, String estado, Integer page, Integer size)
			throws Exception {
		List<Ordcom> lOrdcom = new ArrayList<>();
		List<Object[]> registros = repo.listaOrdcom(periodo, numoc, estado, page, size);

		registros.forEach(reg -> {
			Proveedor proveedor = new Proveedor();
			proveedor.setIdProveedor(Integer.parseInt(String.valueOf(reg[11])));
			proveedor.setNombre(String.valueOf(reg[12]));
			proveedor.setDireccion(String.valueOf(reg[13]));
			proveedor.setRuc(String.valueOf(reg[14]));
			proveedor.setEstado(String.valueOf(reg[15]));

			Ordcom ordcom = new Ordcom();
			ordcom.setIdOrdcom(Integer.parseInt(String.valueOf(reg[0])));
			ordcom.setPeriodo(Integer.parseInt(String.valueOf(reg[1])));
			ordcom.setNumoc(Integer.parseInt(String.valueOf(reg[2])));
			ordcom.setFecha(LocalDate.parse(String.valueOf(reg[3])));
			ordcom.setDocref(String.valueOf(reg[4]));
			ordcom.setNumref(String.valueOf(reg[5]));
			ordcom.setFecref(LocalDate.parse(String.valueOf(reg[6])));
			ordcom.setProveedor(proveedor);
			ordcom.setDestino(String.valueOf(reg[17]));
			ordcom.setGigv(Boolean.parseBoolean(String.valueOf(reg[18])));
			ordcom.setTotoc(Double.parseDouble(String.valueOf(reg[19])));
			ordcom.setIgv(Double.parseDouble(String.valueOf(reg[20])));
			ordcom.setObserv(String.valueOf(reg[21]));
			ordcom.setEstado(String.valueOf(reg[22]));

			lOrdcom.add(ordcom);
		});

		return lOrdcom;
	}

	@Override
	public Integer getNewOrdcom(Integer periodo) {
		return repo.getNewOrdcom(periodo);
	}

	@Transactional
	@Override
	public Ordcom registrarOrdcom(Ordcom ordcom) throws Exception {
		Proveedor proveedor = null;
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();

		ordcom.setNumoc(getNewOrdcom(ordcom.getPeriodo()));
		ordcom.setEstado("00");
		ordcom.setUserup(cUser);
		ordcom.setDuserup(dUser);

		if (ordcom.getProveedor().getIdProveedor() == null) {
			proveedor = ordcom.getProveedor();
			proveedor.setEstado("00");
			proveedor = repoProveedor.save(proveedor);
			ordcom.setProveedor(proveedor);
		} else {
			proveedor = repoProveedor.findById(ordcom.getProveedor().getIdProveedor()).orElse(null);
		}
		if (ordcom.getDetordcom()!=null) {
			ordcom.getDetordcom().forEach(det -> {
				det.setOrdcom(ordcom);
				det.setEstado("00");
				det.setUserup(cUser);
				det.setDuserup(dUser);
			});
		}
		Ordcom _ordcom = repo.save(ordcom);

		if (ordcom.getDetordcom()!=null) {
			_ordcom.getDetordcom().forEach(det -> {
				try {
					srvKardex.actualizaKardex(det); // , ordcom
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return _ordcom;
	}

	@Transactional
	@Override
	public Ordcom modificarOrdcom(Ordcom ordcom) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();

		Proveedor _proveedor = (ordcom.getProveedor() != null
				? repoProveedor.getById(ordcom.getProveedor().getIdProveedor())
				: null);

		Ordcom _ordcom = repo.getById(ordcom.getIdOrdcom());

		_ordcom.setFecha(ordcom.getFecha());
		_ordcom.setDocref(ordcom.getDocref());
		_ordcom.setNumref(ordcom.getNumref());
		;
		_ordcom.setFecref(ordcom.getFecref());
		_ordcom.setObserv(ordcom.getObserv());
		_ordcom.setProveedor(_proveedor);
		_ordcom.setDestino(ordcom.getDestino());
		_ordcom.setTotoc(ordcom.getTotoc());
		_ordcom.setIgv(ordcom.getIgv());

		List<Iteoc> _ItemsAgregados = ordcom.getDetordcom().stream()
				.filter(reg -> !_ordcom.getDetordcom().contains(reg)).collect(Collectors.toList());

		List<Iteoc> _ItemsEliminados = _ordcom.getDetordcom().stream()
				.filter(reg -> !ordcom.getDetordcom().contains(reg)).collect(Collectors.toList());

		_ItemsAgregados.forEach(reg -> {
			reg.setOrdcom(_ordcom);
			reg.setEstado("00");
			reg.setUserup(cUser);
			reg.setDuserup(dUser);
			_ordcom.getDetordcom().add(reg);
		});

		_ItemsEliminados.forEach(reg -> {
			Kardex kardex = null;
			try {
				kardex = srvKardex.buscarPorItem("ORC", reg.getOrdcom().getPeriodo(), reg.getOrdcom().getNumoc(),
						reg.getIdIteoc());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (kardex == null) {
				throw new ResourceNotFoundException("Item de Kardex no encontrado");
			}
			Integer periodo = kardex.getPeriodo();
			String tipo = kardex.getTipkar();
			Integer idArticulo = kardex.getArticulo().getIdArticulo();
			Integer correl = kardex.getCorrel();
			Integer ultimo = repoKardex.getUltimoCorrel(periodo, tipo, idArticulo);
			if (correl != ultimo) {
				throw new ResourceNotFoundException(
						"Solo se puede eliminar el ultimo item de la targeta. Por favor revise su kardex ");
			}
			try {
				srvKardex.eliminar(kardex.getIdKardex());
				srvKardex.recalcular(periodo, tipo, idArticulo);
//				srvIteoc.eliminar(reg.getIdIteoc());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Ordcom _ordcomGrabada = repo.save(_ordcom);

		if (_ordcomGrabada != null) {

			_ordcomGrabada.getDetordcom().forEach(det -> {
				try {
					srvKardex.actualizaKardex(det); // , pecosa
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

//			_ItemsEliminados.forEach(reg -> {
//				Kardex kardex = null;
//				try {
//					kardex = srvKardex.buscarPorItem("ORC", reg.getOrdcom().getPeriodo(), reg.getOrdcom().getNumoc(),
//							reg.getIdIteoc());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (kardex == null) {
//					throw new ResourceNotFoundException("Item de Kardex no encontrado");
//				}
//				Integer periodo = kardex.getPeriodo();
//				String tipo = kardex.getTipkar();
//				Integer idArticulo = kardex.getArticulo().getIdArticulo();
//				Integer correl = kardex.getCorrel();
//				Integer ultimo = repoKardex.getUltimoCorrel(periodo, tipo, idArticulo);
//				if (correl != ultimo) {
//					throw new ResourceNotFoundException(
//							"Solo se puede eliminar el ultimo item de la targeta. Por favor revise su kardex ");
//				}
//				try {
//					srvKardex.eliminar(kardex.getIdKardex());
//					srvKardex.recalcular(periodo, tipo, idArticulo);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
		}
		return _ordcomGrabada;
	}

	@Override
	public byte[] reporteOrdcom(int id) throws Exception {
		byte[] data = null;
		Ordcom ordcom = repo.findById(id).orElse(null);
		List<Iteoc> lIteoc = null;
		if (ordcom!=null) {
			lIteoc = ordcom.getDetordcom();
		}
		log.info("reporteOrdcom...");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ordcom", ordcom);
		parametros.put("lIteoc", lIteoc);
		log.info("reporteOrdcom...antes de:");

		PDFOrdcom pdfOrdcom = new PDFOrdcom(parametros);

		return pdfOrdcom.creaOrdcom();
	}

}
