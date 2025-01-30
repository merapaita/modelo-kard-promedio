package com.rosist.kardex.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.model.Iteoc;
import com.rosist.kardex.model.Itepedido;
import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Stock;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.repo.IStockRepo;
import com.rosist.kardex.reportes.PDFExistenciasMes;
import com.rosist.kardex.reportes.PDFKardex;
import com.rosist.kardex.reportes.PDFStock;
import com.rosist.kardex.service.IArticuloService;
import com.rosist.kardex.service.IKardexService;

@Service
public class KardexServiceImpl extends CRUDImpl<Kardex, Integer> implements IKardexService {

	@Autowired
	private IKardexRepo repo;

	@Autowired
	private IStockRepo repoStock;

	@Autowired
	private DriverManagerDataSource datasource;

	@Autowired
	private IArticuloService articuloService;

	private static final Logger log = LoggerFactory.getLogger(KardexServiceImpl.class);

	@Override
	protected IGenericRepo<Kardex, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Kardex> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public Integer getNewCorrel(Integer periodo, String tipkar, Integer id_articulo) {
		return repo.getNewCorrel(periodo, tipkar, id_articulo);
	}

	@Override
	public void actualizaKardex(Iteii iteii) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();

		Kardex kardex = new Kardex();
		Integer periodo = iteii.getInvini().getPeriodo();
		String tipkar = iteii.getTipkar();
		Integer idArticulo = iteii.getArticulo().getIdArticulo();
		double valorPromedio = iteii.getValuni();
		double saldoCantidad = iteii.getCantidad();
		double valorPromedioFraccion = iteii.getValunifr();
		double saldoTotalCantidad = iteii.getTotcan();

		Stock stock = repoStock.buscaPorArticulo(periodo, tipkar, idArticulo);
		if (stock != null) {

			valorPromedio = (stock.getSaldoCantidad() * stock.getValorPromedio()
					+ iteii.getCantidad() * iteii.getValuni()) / (stock.getSaldoCantidad() + iteii.getCantidad());
			valorPromedio = redondear(valorPromedio, 4);
			saldoCantidad = stock.getSaldoCantidad() + iteii.getCantidad();

			valorPromedioFraccion = (stock.getSaldoTotalCantidad() * stock.getValorPromedioFraccion()
					+ iteii.getTotcan() * iteii.getValunifr()) / (stock.getSaldoTotalCantidad() + iteii.getTotcan());
			valorPromedioFraccion = redondear(valorPromedioFraccion, 4);
			saldoTotalCantidad = stock.getSaldoTotalCantidad() + iteii.getTotcan();
		}

		kardex.setPeriodo(periodo);
		kardex.setTipkar(tipkar);
		kardex.setArticulo(iteii.getArticulo());
		int correl = getNewCorrel(periodo, tipkar, idArticulo);
		kardex.setCorrel(correl);
		kardex.setEstado(iteii.getInvini().getEstado());
		kardex.setFecha(iteii.getInvini().getFecha());
		kardex.setTipdoc("INV");
		kardex.setNumdoc(iteii.getInvini().getNumii());
		kardex.setItem(iteii.getIdIteii());
		kardex.setMenudeo(iteii.isMenudeo());
		kardex.setUnidad(iteii.getUnidad());
		kardex.setUnimen(iteii.getUnimen());
		kardex.setCantidad(iteii.getCantidad());
		kardex.setFraccion(iteii.getFraccion());
		kardex.setTotcan(iteii.getTotcan());
		kardex.setTipmov("+");
		kardex.setPreuni(iteii.getPreuni());
		kardex.setIgv(iteii.getIgv());
		kardex.setValuni(iteii.getValuni());
		kardex.setValorPromedio(valorPromedio);
		kardex.setSaldoCantidad(saldoCantidad);
		kardex.setPreunifr(iteii.getPreunifr());
		kardex.setIgvfr(iteii.getIgvfr());
		kardex.setValunifr(iteii.getValunifr());
		kardex.setValorPromedioFraccion(valorPromedioFraccion);
		kardex.setSaldoTotalCantidad(saldoTotalCantidad);
		kardex.setUserup(cUser);
		kardex.setDuserup(dUser);
		repo.save(kardex);

		actualizaStock(kardex, stock);
	}

	@Override
	public void actualizaKardex(Iteoc iteoc) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		Integer periodo = iteoc.getOrdcom().getPeriodo();
		String tipkar = iteoc.getTipkar();
		Integer idArticulo = iteoc.getArticulo().getIdArticulo();
		double valorPromedio = iteoc.getValuni();
		double saldoCantidad = iteoc.getCantidad();
		double valorPromedioFraccion = iteoc.getValunifr();
		double saldoTotalCantidad = iteoc.getTotcan();

		Kardex _kardex = repo.getBuscaPorItem("ORC", iteoc.getOrdcom().getPeriodo(), iteoc.getOrdcom().getNumoc(), iteoc.getIdIteoc());
		if (_kardex==null) {
			Stock stock = repoStock.buscaPorArticulo(periodo, tipkar, idArticulo);
			if (stock != null) {
				valorPromedio = (stock.getSaldoCantidad() * stock.getValorPromedio()
						+ iteoc.getCantidad() * iteoc.getValuni()) / (stock.getSaldoCantidad() + iteoc.getCantidad());
				valorPromedio = redondear(valorPromedio, 4);
				
				saldoCantidad = stock.getSaldoCantidad() + iteoc.getCantidad();
				
				valorPromedioFraccion = (stock.getSaldoTotalCantidad() * stock.getValorPromedioFraccion()
						+ iteoc.getTotcan() * iteoc.getValunifr()) / (stock.getSaldoTotalCantidad() + iteoc.getTotcan());
				valorPromedioFraccion = redondear(valorPromedioFraccion, 4);
				
				saldoTotalCantidad = stock.getSaldoTotalCantidad() + iteoc.getTotcan();
			}
			
			Kardex kardex = new Kardex();
			kardex.setPeriodo(periodo);
			kardex.setTipkar(tipkar);
			kardex.setArticulo(iteoc.getArticulo());
			int correl = getNewCorrel(periodo, tipkar, idArticulo);
			kardex.setCorrel(correl);
			kardex.setEstado(iteoc.getOrdcom().getEstado());
			kardex.setFecha(iteoc.getOrdcom().getFecha());
			kardex.setTipdoc("ORC");
			kardex.setNumdoc(iteoc.getOrdcom().getNumoc());
			kardex.setItem(iteoc.getIdIteoc());
			kardex.setMenudeo(iteoc.isMenudeo());
			kardex.setUnidad(iteoc.getUnidad());
			kardex.setUnimen(iteoc.getUnimen());
			kardex.setCantidad(iteoc.getCantidad());
			kardex.setFraccion(iteoc.getFraccion());
			kardex.setTotcan(iteoc.getTotcan());
			kardex.setTipmov("+");
			kardex.setPreuni(iteoc.getPreuni());
			kardex.setIgv(iteoc.getIgv());
			kardex.setValuni(iteoc.getValuni());
			kardex.setValorPromedio(valorPromedio);
			kardex.setSaldoCantidad(saldoCantidad);
			kardex.setPreunifr(iteoc.getPreunifr());
			kardex.setIgvfr(iteoc.getIgvfr());
			kardex.setValunifr(iteoc.getValunifr());
			kardex.setValorPromedioFraccion(valorPromedioFraccion);
			kardex.setSaldoTotalCantidad(saldoTotalCantidad);
			kardex.setUserup(cUser);
			kardex.setDuserup(dUser);
			repo.save(kardex);
			
			actualizaStock(kardex, stock);
		}

	}

	@Override
	public void actualizaKardex(Itepedido itepedido) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();
		Integer periodo = itepedido.getPedido().getPeriodo();
		String tipkar = itepedido.getTipkar();
		Integer idArticulo = itepedido.getArticulo().getIdArticulo();
		double valorPromedio = itepedido.getValuni();
		double saldoCantidad = 0;
		double valorPromedioFraccion = itepedido.getValunifr();
		double saldoTotalCantidad = itepedido.getTotcan();

		Stock stock = repoStock.buscaPorArticulo(periodo, tipkar, idArticulo);
		if (stock != null) {
			valorPromedio = stock.getValorPromedio();
			valorPromedioFraccion = stock.getValorPromedioFraccion();
			saldoCantidad = stock.getSaldoCantidad() - itepedido.getCantidad();
			saldoTotalCantidad = stock.getSaldoTotalCantidad() - itepedido.getTotcan();
		}

		Kardex kardex = new Kardex();
		kardex.setPeriodo(itepedido.getPedido().getPeriodo());
		kardex.setTipkar(itepedido.getTipkar());
		kardex.setArticulo(itepedido.getArticulo());
		int correl = getNewCorrel(periodo, tipkar, idArticulo);
		kardex.setCorrel(correl);
		kardex.setEstado(itepedido.getEstado());
		kardex.setFecha(itepedido.getPedido().getFecha());
		kardex.setTipdoc("PEC");
		kardex.setNumdoc(itepedido.getPedido().getNumped());
		kardex.setItem(itepedido.getIdItepedido());
		kardex.setMenudeo(itepedido.isMenudeo());
		kardex.setUnidad(itepedido.getUnidad());
		kardex.setUnimen(itepedido.getUnimen());
		kardex.setCantidad(itepedido.getCantidad());
		kardex.setFraccion(itepedido.getFraccion());
		kardex.setTotcan(itepedido.getTotcan());
		kardex.setTipmov("-");
		kardex.setPreuni(itepedido.getPreuni()); // no
		kardex.setIgv(itepedido.getIgv()); // no
		kardex.setValuni(itepedido.getValuni()); // no
		kardex.setValorPromedio(valorPromedio);
		kardex.setSaldoCantidad(saldoCantidad);
		kardex.setPreunifr(itepedido.getPreunifr()); // no
		kardex.setIgvfr(itepedido.getIgvfr()); // no
		kardex.setValunifr(itepedido.getValunifr()); // no
		kardex.setValorPromedioFraccion(valorPromedioFraccion);
		kardex.setSaldoTotalCantidad(saldoTotalCantidad);
		kardex.setUserup(cUser);
		kardex.setDuserup(dUser);
		repo.save(kardex);

		actualizaStock(kardex, stock);
	}

	private void actualizaStock(Kardex kardex, Stock stock) {
		Integer periodo = kardex.getPeriodo();
		String tipkar = kardex.getTipkar();

		double cantidad = 0.0;
		double totalCantidad = 0.0;
		double valorPromedio = 0.0;
		double valorPromedioFraccion = 0.0;
		if (stock != null) {
			if (kardex.getTipmov().equals("+")) {
				valorPromedio = (stock.getSaldoCantidad() * stock.getValorPromedio()
						+ kardex.getCantidad() * kardex.getValuni())
						/ (stock.getSaldoCantidad() + kardex.getCantidad());
				valorPromedio = redondear(valorPromedio, 4);
				
				stock.setValorPromedio(valorPromedio);

				valorPromedioFraccion = (stock.getSaldoTotalCantidad() * stock.getValorPromedioFraccion()
						+ kardex.getTotcan() * kardex.getValunifr())
						/ (stock.getSaldoTotalCantidad() + kardex.getTotcan());
				valorPromedioFraccion = redondear(valorPromedioFraccion, 4);
				
				stock.setValorPromedioFraccion(valorPromedioFraccion);
				cantidad = stock.getSaldoCantidad() + kardex.getCantidad();
				totalCantidad = stock.getSaldoTotalCantidad() + kardex.getTotcan();
			} else if (kardex.getTipmov().equals("-")) {
				cantidad = stock.getSaldoCantidad() - kardex.getCantidad();
				totalCantidad = stock.getSaldoTotalCantidad() - kardex.getTotcan();
			}
			stock.setSaldoCantidad(cantidad);
			stock.setSaldoTotalCantidad(totalCantidad);
		} else {
			stock = new Stock();
			stock.setPeriodo(periodo);
			stock.setTipkar(tipkar);
			stock.setArticulo(kardex.getArticulo());
			stock.setEstado("00");
			stock.setSaldoCantidad(kardex.getCantidad());
			stock.setFraccion(kardex.getFraccion());
			stock.setSaldoTotalCantidad(kardex.getSaldoTotalCantidad());
			stock.setValorPromedio(kardex.getValorPromedio());
			stock.setValorPromedioFraccion(kardex.getValorPromedioFraccion());
		}
		repoStock.save(stock);
	}

	@Override
	public void recalcular(Integer periodo, String tipkar, Integer idArticulo) throws Exception {
		String cUser = "cUser";
//		String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime dUser = LocalDateTime.now();

		List<Kardex> listaKardex = repo.kardexPorarticulo(periodo, tipkar, idArticulo);

		double saldoCantidad = 0.0;
		double saldoTotalCantidad = 0.0;
		double valorPromedio = 0.0;
		double valorPromedioFraccion = 0.0;

		for (Kardex registro : listaKardex) {
			boolean graba = false;
			if (registro.getTipmov().equals("+")) {
				double xx = saldoCantidad * valorPromedio;
				double yy = registro.getCantidad() * registro.getValuni();

				valorPromedio = (xx + yy) / (saldoCantidad + registro.getCantidad());
				valorPromedio = redondear(valorPromedio, 4);

				valorPromedioFraccion = (saldoTotalCantidad * valorPromedioFraccion
						+ registro.getTotcan() * registro.getValunifr()) / (saldoTotalCantidad + registro.getTotcan());
				valorPromedioFraccion = redondear(valorPromedioFraccion, 4);

				saldoCantidad += registro.getCantidad();

				saldoTotalCantidad += registro.getTotcan();

				if (valorPromedio != registro.getValorPromedio()
						|| valorPromedioFraccion != registro.getValorPromedioFraccion()
						|| saldoCantidad != registro.getSaldoCantidad()
						|| saldoTotalCantidad != registro.getSaldoTotalCantidad()) {
					registro.setValorPromedio(valorPromedio);
					registro.setValorPromedioFraccion(valorPromedioFraccion);
					registro.setSaldoCantidad(saldoCantidad);
					registro.setSaldoTotalCantidad(saldoTotalCantidad);
					registro.setUsercr(cUser);
					registro.setDusercr(dUser);
					graba = true;
				}
			} else if (registro.getTipmov().equals("-")) {
				saldoCantidad -= registro.getCantidad();

				saldoTotalCantidad -= registro.getTotcan();

				if (saldoCantidad != registro.getSaldoCantidad()
						|| saldoTotalCantidad != registro.getSaldoTotalCantidad()) {
					registro.setSaldoCantidad(saldoCantidad);
					registro.setSaldoTotalCantidad(saldoTotalCantidad);
					registro.setUsercr(cUser);
					registro.setDusercr(dUser);
					graba = true;
				}
			}
			if (graba) {
				repo.save(registro);
			}
		}

		Stock stock = repoStock.buscaPorArticulo(periodo, tipkar, idArticulo);
		if (stock != null) {
			stock.setValorPromedio(valorPromedio);
			stock.setValorPromedioFraccion(valorPromedioFraccion);
			stock.setSaldoCantidad(saldoCantidad);
			stock.setSaldoTotalCantidad(saldoCantidad);
			repoStock.save(stock);
		}
	}

	@Override
	public List<Stock> listarStock(Integer periodo, String tipkar, String descri, Integer mes) {
		List<Stock> lStock = new ArrayList<>();
		List<Object[]> arrRpta;

		arrRpta = repo.getStock(periodo, tipkar, descri, mes);

		arrRpta.forEach(x -> {
			Stock stock = new Stock();
			stock.setPeriodo(Integer.parseInt(String.valueOf(x[0])));
			stock.setTipkar(String.valueOf(x[1]));
//			2 desTipkar

			Articulo articulo;
			try {
				articulo = articuloService.listarPorId(Integer.parseInt(String.valueOf(x[3])));
				stock.setArticulo(articulo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stock.setCorrel(Integer.parseInt(String.valueOf(x[12])));
			stock.setEstado(String.valueOf(x[13]));
			stock.setEntrada(Double.parseDouble(String.valueOf(x[16]))); // ?
			stock.setSalida(Double.parseDouble(String.valueOf(x[17]))); // ?
			stock.setSaldoCantidad(Double.parseDouble(String.valueOf(x[18])));
			stock.setFraccion(Double.parseDouble(String.valueOf(x[19])));
			stock.setSaldoTotalCantidad(Double.parseDouble(String.valueOf(x[20])));
			stock.setValorPromedio(Double.parseDouble(String.valueOf(x[23])));
			stock.setValorPromedioFraccion(Double.parseDouble(String.valueOf(x[26])));
			lStock.add(stock);

		});
		return lStock;
	}

	@Override
	public Page<Stock> listarStock(Integer periodo, String tipkar, String descri, Integer mes, Integer page,
			Integer size) {
		List<Stock> lStock = new ArrayList<>();

		lStock = listarStock(periodo, tipkar, descri, mes);

		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), lStock.size()); // allCustomers.size()
		List<Stock> pageContent = lStock.subList(start, end);

		return new PageImpl<>(pageContent, pageRequest, lStock.size());
	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
	}

	@Override
	public List<Kardex> listarKardex(Integer periodo, String tipkar, Integer idArticulo, Integer correl) {
		List<Kardex> lKardex = new ArrayList<>();
		List<Object[]> arrRpta;

		arrRpta = repo.getKardex(periodo, tipkar, idArticulo, correl);

		arrRpta.forEach(x -> {
			Articulo articulo = new Articulo();
			articulo.setIdArticulo(Integer.parseInt(String.valueOf(x[3])));
			articulo.setCodart(String.valueOf(x[4]));
			articulo.setNomart(String.valueOf(x[5]));
			articulo.setMenudeo(Boolean.parseBoolean(String.valueOf(x[6])));
			articulo.setFraccion(Integer.parseInt(String.valueOf(x[7])));
			articulo.setUnidad(String.valueOf(x[8]));
//			articulo.setUnimen(String.valueOf(x[9]));

			Kardex kardex = new Kardex();
			kardex.setIdKardex(Integer.parseInt(String.valueOf(x[0])));
			kardex.setPeriodo(Integer.parseInt(String.valueOf(x[1])));
			kardex.setTipkar(String.valueOf(x[2]));
			kardex.setArticulo(articulo);
			kardex.setCorrel(Integer.parseInt(String.valueOf(x[10])));
//			kardex.setCoraux(Integer.parseInt(String.valueOf(x[11])));
			kardex.setEstado(String.valueOf(x[12]));
			kardex.setFecha(LocalDate.parse(String.valueOf(x[13])));
			kardex.setTipdoc(String.valueOf(x[14]));
			kardex.setMenudeo(Boolean.parseBoolean(String.valueOf(x[15])));
			kardex.setNumdoc(Integer.parseInt(String.valueOf(x[16])));
			kardex.setItem(Integer.parseInt(String.valueOf(x[17])));
			kardex.setCantidad(Double.parseDouble(String.valueOf(x[18])));
			kardex.setFraccion(Double.parseDouble(String.valueOf(x[19])));
			kardex.setTotcan(Double.parseDouble(String.valueOf(x[20])));
			kardex.setSaldoCantidad(Double.parseDouble(String.valueOf(x[21])));
			kardex.setTipmov(String.valueOf(x[22]));
			
			if (kardex.getTipmov().equals("+")) {
				kardex.setEntrada(kardex.getTotcan());
//				saldo += kardex.getTotcan();
//				kardex.setSaldoCantidad(saldo);
			} else {
				kardex.setSalida(kardex.getTotcan());
//				saldo -= reg.getTotcan();
//				reg.setSaldoCantidad(saldo);
			}			
			
			kardex.setPreuni(Double.parseDouble(String.valueOf(x[23])));
			kardex.setIgv(Double.parseDouble(String.valueOf(x[24])));
			kardex.setValuni(Double.parseDouble(String.valueOf(x[25])));
			kardex.setPreunifr(Double.parseDouble(String.valueOf(x[26])));
			kardex.setIgvfr(Double.parseDouble(String.valueOf(x[27])));
			kardex.setValunifr(Double.parseDouble(String.valueOf(x[28])));
			kardex.setValorPromedio(Double.parseDouble(String.valueOf(x[29])));
			kardex.setValorPromedioFraccion(Double.parseDouble(String.valueOf(x[30])));

			kardex.setUserup(String.valueOf(x[31]));
			if (x[32] != null)
				kardex.setUsercr(String.valueOf(x[32]));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			if (x[33] != null)
				kardex.setDuserup(LocalDateTime.parse(String.valueOf(x[33]).substring(0, 19), formatter));
			if (x[34] != null)
				kardex.setDusercr(LocalDateTime.parse(String.valueOf(x[34]).substring(0, 19), formatter));

			lKardex.add(kardex);

		});
log.info("lKardex:"+ lKardex);		
		return lKardex;
	}

	@Override
	public Page<Kardex> listarKardex(Integer periodo, String tipkar, Integer idArticulo, Integer correl, Integer page,
			Integer size) {

		List<Kardex> lKardex = listarKardex(periodo, tipkar, idArticulo, correl);

		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), lKardex.size()); // allCustomers.size()
		List<Kardex> pageContent = lKardex.subList(start, end);

		
		return new PageImpl<>(pageContent, pageRequest, lKardex.size());
	}

	@Override
	public List<Kardex> listaKardexPorStock(Integer periodo, String tipkar, Integer idArticulo, Integer correl) {
		/*
		 * aqui corregir pasar de objeto a list<Kardex> y luego si quiero paginacion
		 * agrega la paginacion
		 */
//		List<Object[]> lKardex = repo.getKardex(periodo, tipkar, idArticulo, correl);
//		double saldo = 0.0;
//		for (Kardex reg : lKardex) {
//			if (reg.getTipmov().equals("+")) {
//				reg.setEntrada(reg.getTotcan());
//				saldo += reg.getTotcan();
//				reg.setSaldoCantidad(saldo);
//			} else {
//				reg.setSalida(reg.getTotcan());
//				saldo -= reg.getTotcan();
//				reg.setSaldoCantidad(saldo);
//			}
//		}
		return null;
//		return lKardex;
	}

	@Override
	public Page<Kardex> listaKardexPorStock(Integer periodo, String tipkar, Integer idArticulo, Integer correl,
			Integer page, Integer size) {
		List<Kardex> lKardex = repo.getListaKardexPorStock(periodo, tipkar, idArticulo, correl);

		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), lKardex.size()); // allCustomers.size()
		List<Kardex> pageContent = lKardex.subList(start, end);

		return new PageImpl<>(pageContent, pageRequest, lKardex.size());
	}

	@Override
	public byte[] reporteStock(int periodo, String tipkar, String descri, Integer mes) throws Exception {
		List<Stock> lStock = listarStock(periodo, tipkar, descri, mes).stream()
				.filter(reg -> reg.getSaldoTotalCantidad() != 0.0).collect(Collectors.toList());
		log.info("List<Object[]> lStock:..." + lStock);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("lStock", lStock);
		parametros.put("periodo", periodo);
		parametros.put("mes", mes);
		parametros.put("tipkar", tipkar);

		PDFStock pdfStock = new PDFStock(parametros);

		return pdfStock.creaReporte();
	}

	@Override
	public byte[] reporteKardex(int periodo, String tipkar, Integer idArticulo, String descri) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("datasource", datasource);
		parametros.put("periodo", periodo);
		parametros.put("tipkar", tipkar);
		parametros.put("id_articulo", idArticulo);
		parametros.put("descri", descri);

		PDFKardex pdfKardex = new PDFKardex(parametros);

		return pdfKardex.creaKardex();
	}

	@Override
	public byte[] existenciasMes(Integer periodo, Integer mes, String tipkar) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("datasource", datasource);
		parametros.put("periodo", periodo);
		parametros.put("mes", mes);
		parametros.put("tipkar", tipkar);

		PDFExistenciasMes pruebaPDF = new PDFExistenciasMes(parametros);
		return pruebaPDF.creaReporte();
	}

	@Override
	public Kardex buscarPorItem(String tipdoc, Integer periodo, Integer numdoc, Integer item) throws Exception {
		return repo.getBuscaPorItem(tipdoc, periodo, numdoc, item);
	}
	
    public static double redondear(double valorInicial, int numeroDecimales) {
    	double decimales = (double)Math.pow(10, numeroDecimales );
    	return Math.round(valorInicial * decimales) / decimales;
    }
	
}