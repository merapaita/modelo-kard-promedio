package com.rosist.kardex.model;

import java.time.LocalDateTime;
import java.util.Objects;

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
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "iteii")		// , uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numii"})
public class Iteii {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idIteii;
	
	@NotNull(message = "{iteii.tipkar.null}")
	@Size(min = 1, max = 1, message = "{iteii.tipkar.size}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@NotNull(message = "{iteii.articulo.null}")
	@ManyToOne
	@JoinColumn(name = "id_articulo", nullable = false, foreignKey = @ForeignKey(name = "FK_articuloii"))
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
	@Column(name = "preunifr", nullable = false)
	private double preunifr;
	
	@NotNull(message = "{iteii.igvfr.null}")
	@Column(name = "igvfr", nullable = false)
	private double igvfr;
	
	@NotNull(message = "{iteii.valunifr.null}")
	@Column(name = "valunifr", nullable = false)
	private double valunifr;
	
//	private Precioventa pventa;
//	private double porvta;
//	private double prevta;
//	private double prevtafr;
//	private double porvta2;
//	private double prevta2;
//	private double prevtafr2;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_invini", nullable = false, foreignKey = @ForeignKey(name = "FK_invini"))
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
