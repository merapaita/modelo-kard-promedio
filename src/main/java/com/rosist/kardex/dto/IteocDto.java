package com.rosist.kardex.dto;

import java.time.LocalDateTime;

import com.rosist.kardex.model.Articulo;
import com.rosist.kardex.model.Ordcom;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class IteocDto {

	private Integer idIteoc;
	
	@NotNull(message = "{iteoc.tipkar.null}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
//	@NotNull(message = "{iteoc.item.null}")
	@Column(name = "item", nullable = false, length = 4)
	private Integer item;
	
	@NotNull(message = "{iteoc.articulo.null}")
	private Articulo articulo;
	
	/*posible eliminacion*/
	@Column(name = "menudeo", nullable = false)
	private boolean menudeo;
	
	/*posible eliminacion*/
	@Column(name = "unidad", nullable = true, length = 10)
	private String unidad;
	
	/*posible eliminacion*/
	@Column(name = "unimen", nullable = true, length = 10)
	private String unimen;
	
	@NotNull(message = "{iteoc.cantidad.null}")
	@Column(name = "cantidad", nullable = false)
	private double cantidad;
	
	@NotNull(message = "{iteoc.fraccion.null}")
	@Column(name = "fraccion", nullable = false)
	private double fraccion;
	
	@NotNull(message = "{iteoc.totcan.null}")
	@Column(name = "totcan", nullable = false)
	private double totcan;
	
	@NotNull(message = "{iteoc.precom.null}")
	@Column(name = "precom", nullable = false)
	private double precom;
	
	@NotNull(message = "{iteoc.igvcom.null}")
	@Column(name = "igvcom", nullable = false)
	private double igvcom;
	
	@NotNull(message = "{iteoc.preuni.null}")
	@Column(name = "preuni", nullable = false)
	private double preuni;
	
	@NotNull(message = "{iteoc.igv.null}")
	@Column(name = "igv", nullable = false)
	private double igv;
	
	@NotNull(message = "{iteoc.valuni.null}")
	@Column(name = "valuni", nullable = false)
	private double valuni;
	
	@NotNull(message = "{iteoc.preunifr.null}")
	@Column(name = "preunirf", nullable = false)
	private double preunifr;
	
	@NotNull(message = "{iteoc.igvfr.null}")
	@Column(name = "igvfr", nullable = false)
	private double igvfr;
	
	@NotNull(message = "{iteoc.valunifr.null}")
	@Column(name = "valunifr", nullable = false)
	private double valunifr;

	@Column(name = "estado", nullable = false)
	private String estado;

	@NotNull(message = "{iteoc.ordcom.null}")
	private Ordcom ordcom;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}