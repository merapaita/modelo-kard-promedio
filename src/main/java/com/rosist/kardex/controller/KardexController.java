package com.rosist.kardex.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Stock;
import com.rosist.kardex.repo.IKardexRepo;
import com.rosist.kardex.service.IKardexService;

@RestController
@RequestMapping("/kardex")
public class KardexController {

	@Autowired
	private IKardexRepo repo;
	
	@Autowired
	private IKardexService service;

    private static Logger log = LoggerFactory.getLogger(KardexController.class);
    
	@GetMapping("/listaStock")
	public ResponseEntity<List<Stock>> listarStock(
            @RequestParam(value="periodo", required=true) Integer periodo,
            @RequestParam(value="tipkar", defaultValue = "1") String tipkar,
            @RequestParam(value="descri", defaultValue = "") String descri,
            @RequestParam(value="mes", defaultValue = "0") Integer mes
			) throws Exception{
		return new ResponseEntity<>(service.listarStock(periodo, tipkar, descri, mes), HttpStatus.OK);
	}
	
	@GetMapping("/listaStockPageable")
	public ResponseEntity<Page<Stock>> getAllOficina(
            @RequestParam(value="periodo", required=true) Integer periodo,
            @RequestParam(value="tipkar", defaultValue = "1") String tipkar,
            @RequestParam(value="descri", defaultValue = "") String descri,
            @RequestParam(value="mes", defaultValue = "0") Integer mes,
	        @RequestParam(value="page", defaultValue = "0") Integer page,
	        @RequestParam(value="size", defaultValue = "10") Integer size
		  ) throws Exception {

		Page<Stock> content = service.listarStock(periodo, tipkar, descri, mes, page, size);
		return new ResponseEntity<Page<Stock>>(content, HttpStatus.OK);
	}

	@GetMapping("/listaKardex")
	public ResponseEntity<List<Kardex>> listaKardex(
            @RequestParam(value="periodo", required=true) Integer periodo,
            @RequestParam(value="tipkar", defaultValue = "1") String tipkar,
            @RequestParam(value="idArticulo", required=false) Integer idArticulo,
            @RequestParam(value="correl", required = false) Integer correl,
            @RequestParam(value="page", defaultValue = "-1") Integer page,
            @RequestParam(value="size", defaultValue = "0") Integer size
			) throws Exception{
		return new ResponseEntity<>(service.listaKardexPorStock(periodo, tipkar, idArticulo, correl), HttpStatus.OK);
	}
	
	@GetMapping("/listaKardexPageable")
	public ResponseEntity<Page<Kardex>> listaKardexPageable(
            @RequestParam(value="periodo", required=true) Integer periodo,
            @RequestParam(value="tipkar", defaultValue = "1") String tipkar,
            @RequestParam(value="idArticulo", defaultValue =  "0") Integer idArticulo,
            @RequestParam(value="correl", defaultValue =  "0") Integer correl,
            @RequestParam(value="page", defaultValue = "-1") Integer page,
            @RequestParam(value="size", defaultValue = "0") Integer size
			) throws Exception{
		
		Page<Kardex> content = service.listarKardex(periodo, tipkar, idArticulo, correl, page, size);
		return new ResponseEntity<Page<Kardex>>(content, HttpStatus.OK);
	}

	@GetMapping(value = "/reporteStock", produces = MediaType.APPLICATION_PDF_VALUE) //APPLICATION_PDF_VALUE		//APPLICATION_OCTET_STREAM_VALUE
	public ResponseEntity<byte[]> reporteStock(
            @RequestParam(value="periodo", required=false) Integer periodo,
            @RequestParam(value="tipkar", defaultValue = "1") String tipkar,
            @RequestParam(value="descri", defaultValue = "") String descri,
            @RequestParam(value="mes", defaultValue = "0") Integer mes
			) throws Exception {
		byte[] data = null;

		data = service.reporteStock(periodo, tipkar, descri, mes);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	@GetMapping(value = "/reporteKardex", produces = MediaType.APPLICATION_PDF_VALUE) //APPLICATION_PDF_VALUE		//APPLICATION_OCTET_STREAM_VALUE
	public ResponseEntity<byte[]> reporteKardex(
            @RequestParam(value="periodo", required=false) Integer periodo,
            @RequestParam(value="tipkar", required=false) String tipkar,
            @RequestParam(value="idArticulo", required=false) Integer idArticulo,
            @RequestParam(value="descri", required=false) String descri
			) throws Exception {
		byte[] data = service.reporteKardex(periodo, tipkar, idArticulo, descri);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	@GetMapping(value = "/ExistenciasMes", produces = MediaType.APPLICATION_PDF_VALUE) //APPLICATION_PDF_VALUE		//APPLICATION_OCTET_STREAM_VALUE
	public ResponseEntity<byte[]> existenciasMes(
            @RequestParam(value="periodo", required=false) Integer periodo,
            @RequestParam(value="mes", required=false) Integer mes,
            @RequestParam(value="tipkar", required=false) String tipkar
			) throws Exception {
		byte[] data = null;
			data = service.existenciasMes(periodo, mes, tipkar);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
}