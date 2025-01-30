package com.rosist.kardex.service.impl;

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
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Empleado;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Pedido;
import com.rosist.kardex.repo.IArticuloRepo;
import com.rosist.kardex.repo.IEmpleadoRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IItepedidoRepo;
import com.rosist.kardex.repo.IPedidoRepo;
import com.rosist.kardex.reportes.PDFPedido;
import com.rosist.kardex.service.IItepedidoService;
import com.rosist.kardex.service.IKardexService;
import com.rosist.kardex.service.IPedidoService;

@Service
public class PedidoServiceImpl extends CRUDImpl<Pedido, Integer> implements IPedidoService {

	@Autowired
	private IPedidoRepo repo;

	@Autowired
	private IItepedidoRepo repoItepec;

//	@Autowired
//	private IItepedidoService srvItepedido;

	@Autowired
	private IKardexService srvKardex;

	@Autowired
	private IArticuloRepo repoArticulo;

	@Autowired
	private IEmpleadoRepo repoEmpleado;

	private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

	@Override
	protected IGenericRepo<Pedido, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Pedido> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public List<Pedido> listarPedido(Integer periodo, Integer numpec, String estado, Integer page, Integer size)
			throws Exception {
		List<Pedido> lPecosa = new ArrayList<>();
		List<Object[]> registros = repo.listarPedido(periodo, numpec, estado, page, size);

		registros.forEach(reg -> {
			Empleado empleado = new Empleado();
			empleado.setIdEmpleado(Integer.parseInt(String.valueOf(reg[10])));
			empleado.setNombre(String.valueOf(reg[11]));

			Pedido pedido = new Pedido();
			pedido.setIdPedido(Integer.parseInt(String.valueOf(reg[0])));
			pedido.setPeriodo(Integer.parseInt(String.valueOf(reg[1])));
			pedido.setTipkar(String.valueOf(reg[2]));
			pedido.setNumped(Integer.parseInt(String.valueOf(reg[3])));
			pedido.setFecha(LocalDate.parse(String.valueOf(reg[4])));
			pedido.setDocref(String.valueOf(reg[5]));
			pedido.setNumref(String.valueOf(reg[6]));
			pedido.setFecref(LocalDate.parse(String.valueOf(reg[7])));

			pedido.setEmpleado(empleado);
			pedido.setTotped(Double.parseDouble(String.valueOf(reg[17])));
			pedido.setValped(Double.parseDouble(String.valueOf(reg[18])));
			pedido.setTotvta(Double.parseDouble(String.valueOf(reg[19])));
			pedido.setObserv(String.valueOf(reg[20]));
			pedido.setEstado(String.valueOf(reg[21]));

			lPecosa.add(pedido);
		});

		return lPecosa;
	}

	@Override
	public Integer getNewPedido(Integer periodo) {
		return repo.getNewPedido(periodo);
	}

	@Transactional
	@Override
	public Pedido registrarPedido(Pedido pedido) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		pedido.setNumped(getNewPedido(pedido.getPeriodo()));
		pedido.setEstado("00");
		pedido.setUserup(cUser);
		pedido.setDuserup(dUser);

		if (pedido.getDocref().isBlank()) {
			throw new ResourceNotFoundException("no se ha agregado Documento de referencia");
		}
		if (pedido.getNumref().isBlank()) {
			throw new ResourceNotFoundException("no se ha agregado Numero de Documento de referencia");
		}
		if (pedido.getEmpleado().getIdEmpleado() == null) {
			throw new ResourceNotFoundException("Id de Empleado no ubicado");
		}
		
		if (pedido.getDetpedido()!=null) {
			pedido.getDetpedido().forEach(det -> {
				Articulo articulo = repoArticulo.findById(det.getArticulo().getIdArticulo()).orElse(null);
				det.setPedido(pedido);
				det.setArticulo(articulo);
				det.setEstado("00");
				det.setUserup(cUser);
				det.setDuserup(dUser);
			});
		}
		Pedido _pedido = repo.save(pedido);
		if (pedido.getDetpedido()!=null) {
			_pedido.getDetpedido().forEach(det -> {
				try {
					srvKardex.actualizaKardex(det);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return pedido;
	}
	
	@Transactional
	@Override
	public Pedido modificarPedido(Pedido pedido) throws Exception {
        String cUser = "cUser";
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		
		Empleado _empleado = (pedido.getEmpleado()!=null?repoEmpleado.findById(pedido.getEmpleado().getIdEmpleado()).orElse(null):null);
		
		Pedido _pedido = repo.findById(pedido.getIdPedido()).orElse(pedido);
		
		_pedido.setFecha(pedido.getFecha());
		_pedido.setDocref(pedido.getDocref());
		_pedido.setNumref(pedido.getNumref());;
		_pedido.setFecref(pedido.getFecref());
		_pedido.setObserv(pedido.getObserv());
		_pedido.setTotped(pedido.getTotped());
		_pedido.setValped(pedido.getValped());
		_pedido.setTotvta(pedido.getTotvta());
		_pedido.setEmpleado(_empleado);

		List<Itepedido> _ItemsAgregados = pedido.getDetpedido().stream()
				.filter(reg -> !_pedido.getDetpedido().contains(reg))
				.collect(Collectors.toList());
		
		List<Itepedido> _ItemsEliminados = _pedido.getDetpedido().stream()
				.filter(reg -> !pedido.getDetpedido().contains(reg))
				.collect(Collectors.toList());

		_ItemsAgregados.forEach(reg -> {
			reg.setPedido(_pedido);
			reg.setEstado("00");
			reg.setUserup(cUser);
			reg.setDuserup(dUser);
			_pedido.getDetpedido().add(reg);
		});
		
		Pedido _pedidoGrabada = repo.save(_pedido);

		if (_pedidoGrabada!=null) {
			
			_pedidoGrabada.getDetpedido().forEach(det -> {
				try {
					srvKardex.actualizaKardex(det); // , pedido
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			_ItemsEliminados.forEach(reg -> {
				try {
					Kardex kardex = srvKardex.buscarPorItem("PEC", reg.getPedido().getPeriodo(), reg.getPedido().getNumped(), reg.getIdItepedido());
					if (kardex != null) {
						log.info("eliminando kardex:" + kardex.getIdKardex());			
						srvKardex.eliminar(kardex.getIdKardex());
						repoItepec.eliminaItem(reg.getIdItepedido());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}
		
		return _pedidoGrabada;

	}
	
	@Override
	public byte[] reportePedido(int id) throws Exception {
		byte[] data = null;
		log.info("reportePecosa...id:" + id);
		Pedido pedido = repo.findById(id).orElse(null);
		log.info("reportePecosa...id:" + id);
//		List<Itepec> lItepec = srvItepec.listaPorPecosa(id);
//        logger.info("reporteAnalisis..." + analisis);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pedido", pedido);
//		parametros.put("lItepec", lItepec);

		PDFPedido pdfPedido = new PDFPedido(parametros);

		return pdfPedido.creaPedido();
	}

}