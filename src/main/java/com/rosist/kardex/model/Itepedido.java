package com.rosist.kardex.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "itepedido")		// , uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numii"})
public class Itepedido {

    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idItepedido;
	
	@NotNull(message = "{itepedido.tipkar.null}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@Column(name = "item", nullable = false, length = 4)
	private int item;
	
	@NotNull(message = "{itepedido.articulo.null}")
	@ManyToOne
	@JoinColumn(name = "id_articulo", nullable = false, foreignKey = @ForeignKey(name = "FK_articulopec"))
	private Articulo articulo;
	
	@Column(name = "correl", nullable = false, length = 4)
	private int correl;
	
	/*posible eliminacion*/
	@Column(name = "menudeo", nullable = false)
	private boolean menudeo;
	
	/*posible eliminacion*/
	@Column(name = "unidad", nullable = true, length = 10)
	private String unidad;
	
	/*posible eliminacion*/
	@Column(name = "unimem", nullable = true, length = 10)
	private String unimen;
	
	@Column(name = "cantidad", nullable = false)
	private double cantidad;
	
	@Column(name = "fraccion", nullable = false)
	private double fraccion;
	
	@Column(name = "totcan", nullable = false)
	private double totcan;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio solo valor */
	@Column(name = "monto", nullable = false)
	private double monto;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio ni igv solo valor */
	@Column(name = "igvmto", nullable = false)
	private double igvmto;
	
	/* aqui se grabara el valor total (cantidad x valuni) */
	@Column(name = "valor", nullable = false)
	private double valor;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio solo valor */
	@Column(name = "preuni", nullable = false)
	private double preuni;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio solo valor */
	@Column(name = "igv", nullable = false)
	private double igv;
	
	/* aqui se graba el valor promedio captado del kardex */
	@Column(name = "valuni", nullable = false)
	private double valuni;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio solo valor */
	@Column(name = "preunifr", nullable = false)
	private double preunifr;
	
	/* posiblemente no se grabe nada ya que aqui no hay precio solo valor */
	@Column(name = "igvfr", nullable = false)
	private double igvfr;
	
	/* aqui se graba el valor promedio de la fraccion captado del kardex*/
	@Column(name = "valunifr", nullable = false)
	private double valunifr;

	/* posiblemente se elimine ya que nunca se grabo nada */
	@Column(name = "total", nullable = false)
	private double total;
	
	/* posiblemente se elimine ya que nunca se grabo nada */
	@Column(name = "igvtotal", nullable = false)
	private double igvtotal;
	
	/* posiblemente se elimine ya que nunca se grabo nada */
	@Column(name = "valtotal", nullable = false)
	private double valtotal;
	
	@Column(name = "prevta", nullable = false)
	private double prevta;
	
	@Column(name = "igvvta", nullable = false)
	private double igvvta;
	
	@Column(name = "valvta", nullable = false)
	private double valvta;
	
	@Column(name = "prevtafr", nullable = false)
	private double prevtafr;
	
	@Column(name = "igvvtafr", nullable = false)
	private double igvvtafr;
	
	@Column(name = "valvtafr", nullable = false)
	private double valvtafr;

	@Column(name = "estado", nullable = false)
	private String estado;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_pedido", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido"))
	private Pedido pedido;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}
