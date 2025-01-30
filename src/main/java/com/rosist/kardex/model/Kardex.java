package com.rosist.kardex.model;

import java.time.LocalDate;
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
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "kardex", uniqueConstraints=@UniqueConstraint(columnNames={"periodo", "tipkar", "id_articulo", "correl"}))	// , "coraux"
public class Kardex {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idKardex;
	
	@Column(name = "periodo", nullable = false, length = 4)
	private Integer periodo;
	
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@Transient
	private String desTipkar;
	
	@ManyToOne
	@JoinColumn(name = "id_articulo", nullable = false, foreignKey = @ForeignKey(name = "FK_articulo1"))
	private Articulo articulo;
	
	@Column(name = "correl", nullable = false, length = 4)
	private Integer correl;

	@Column(name = "coraux", nullable = true, length = 4)
	private Integer coraux;
	
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@Column(name = "tipdoc", nullable = false, length = 3)
	private String tipdoc;
	
	@Column(name = "numdoc", nullable = false, length = 4)
	private Integer numdoc;
	
	@Column(name = "item", nullable = false, length = 4)
	private Integer item;
	
	/*posible eliminacion*/
	@Column(name = "menudeo", nullable = true)
	private Boolean menudeo;
	
	/*posible eliminacion*/
	@Column(name = "unidad", nullable = true, length = 10)
	private String unidad;
	
	/*posible eliminacion*/
	@Column(name = "unimen", nullable = true, length = 10)
	private String unimen;
	
	@Column(name = "cantidad", nullable = false)
	private double cantidad;
	
	@Column(name = "fraccion", nullable = false)
	private double fraccion;
	
	@Column(name = "totcan", nullable = false)
	private Double totcan;
	
	@Column(name = "tipmov", nullable = false, length = 1)
	private String tipmov;
	
	@Transient
	private double entrada;
	
	@Transient
	private double salida;
	
	@Column(name = "saldo_cantidad", nullable = false)
	private double saldoCantidad;
	
	@Column(name = "saldo_total_cantidad", nullable = false)
	private double saldoTotalCantidad;
	
	@Column(name = "preuni", nullable = false)
	private double preuni;
	
	@Column(name = "igv", nullable = false)
	private double igv;
	
	@Column(name = "valuni", nullable = false)
	private double valuni;
	
	@Column(name = "valor_promedio", nullable = false)
	private double valorPromedio;
	
	@Column(name = "preunifr", nullable = false)
	private double preunifr;
	
	@Column(name = "igvfr", nullable = false)
	private double igvfr;
	
	@Column(name = "valunifr", nullable = false)
	private double valunifr;
	
	@Column(name = "valor_promedio_fraccion", nullable = false)
	private double valorPromedioFraccion;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}
