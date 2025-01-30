package com.rosist.kardex.model;

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
@Table(name = "stock", uniqueConstraints=@UniqueConstraint(columnNames={"periodo", "tipkar", "id_articulo"}))	// , "correl", "coraux"
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idStock;
	
	@Column(name = "periodo", nullable = false, length = 4)
	private int periodo;
	
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@ManyToOne
	@JoinColumn(name = "id_articulo", nullable = false, foreignKey = @ForeignKey(name = "FK_articulo_kardex"))
	private Articulo articulo;
	
	@Column(name = "correl", nullable = false, length = 4)
	private int correl;
	
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@Transient
	private String fecha;
	
	@Transient
	private double entrada;
	
	@Transient
	private double salida;
	
	@Column(name = "saldo_cantidad", nullable = false)
	private double saldoCantidad;
	
	@Column(name = "fraccion", nullable = false)
	private double fraccion;
	
	@Column(name = "saldo_total_cantidad", nullable = false)
	private double saldoTotalCantidad;

	@Column(name = "precio_promedio", nullable = false)
	private double precioPromedio;

	@Column(name = "igv_promedio", nullable = false)
	private double igvPromedio;

	@Column(name = "valor_promedio", nullable = false)
	private double valorPromedio;
	
	@Column(name = "precio_fraccion_promedio", nullable = false)
	private double precioPromedioFraccion;

	@Column(name = "igv_fraccion_promedio", nullable = false)
	private double igvPromedioFraccion;

	@Column(name = "valor_fraccion_promedio", nullable = false)
	private double valorPromedioFraccion;

}