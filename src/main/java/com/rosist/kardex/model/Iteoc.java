package com.rosist.kardex.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "iteoc") // , uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numii"})
public class Iteoc {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idIteoc;

	@NotNull(message = "{iteoc.tipkar.null}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;

	@NotNull(message = "{iteoc.item.null}")
	@Column(name = "item", nullable = false, length = 4)
	private Integer item;

	@NotNull(message = "{iteoc.articulo.null}")
	@ManyToOne
	@JoinColumn(name = "id_articulo", nullable = false, foreignKey = @ForeignKey(name = "FK_articulo"))
	private Articulo articulo;

	/* posible eliminacion */
	@Column(name = "menudeo", nullable = false)
	private boolean menudeo;

	/* posible eliminacion */
	@Column(name = "unidad", nullable = true, length = 10)
	private String unidad;

	/* posible eliminacion */
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
	@Column(name = "preunifr", nullable = false)
	private double preunifr;

	@NotNull(message = "{iteoc.igvfr.null}")
	@Column(name = "igvfr", nullable = false)
	private double igvfr;

	@NotNull(message = "{iteoc.valunifr.null}")
	@Column(name = "valunifr", nullable = false)
	private double valunifr;
	
//	private Precioventa pventa;
//	private double porvta;
//	private double prevta;
//	private double prevtafr;
//	private double porvta2;
//	private double prevta2;
//	private double prevtafr2;
	
	@Column(name = "estado", nullable = false)
	private String estado;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_ordcom", nullable = false, foreignKey = @ForeignKey(name = "FK_ordcom"))
	private Ordcom ordcom;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "id_kardex", referencedColumnName = "idKardex")
//	private Kardex kardex;

	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;

	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}