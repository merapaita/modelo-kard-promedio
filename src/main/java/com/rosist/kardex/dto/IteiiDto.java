package com.rosist.kardex.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Invini;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IteiiDto {

	private Integer idIteii;
	
	@NotNull(message = "{iteii.tipkar.null}")
	@Size(min = 1, max = 1, message = "{iteii.tipkar.size}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@NotNull(message = "{iteii.articulo.null}")
	private Articulo articulo;
	
	@Column(name = "menudeo", nullable = false)
	private boolean menudeo;
	
	@Column(name = "unidad", nullable = true, length = 10)
	private String unidad;
	
	@Column(name = "unimen", nullable = true, length = 10)
	private String unimen;
	
	@NotNull(message = "{iteii.cantidad.null}")
	@Column(name = "cantidad", nullable = false)
	private double cantidad;
	
	@NotNull(message = "{iteii.fraccion.null}")
	@Column(name = "fraccion", nullable = false)
	private double fraccion;
	
	@NotNull(message = "{iteii.totcan.null}")
	@Column(name = "totcan", nullable = false)
	private double totcan;
	
	@NotNull(message = "{iteii.precom.null}")
	@Column(name = "precom", nullable = false)
	private double precom;
	
	@NotNull(message = "{iteii.igv.null}")
	@Column(name = "igvcom", nullable = false)
	private double igvcom;
	
	@NotNull(message = "{iteii.preuni.null}")
	@Column(name = "preuni", nullable = false)
	private double preuni;
	
	@NotNull(message = "{iteii.igv.null}")
	@Column(name = "igv", nullable = false)
	private double igv;
	
	@NotNull(message = "{iteii.valuni.null}")
	@Column(name = "valuni", nullable = false)
	private double valuni;
	
	@NotNull(message = "{iteii.preunifr.null}")
	@Column(name = "preunirf", nullable = false)
	private double preunifr;
	
	@NotNull(message = "{iteii.igvfr.null}")
	@Column(name = "igvfr", nullable = false)
	private double igvfr;
	
	@NotNull(message = "{iteii.valunifr.null}")
	@Column(name = "valunifr", nullable = false)
	private double valunifr;
	
	private Invini invini;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}
