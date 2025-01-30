package com.rosist.kardex.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "pedido", uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numped"}))
public class Pedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPedido;
	
	@NotNull(message = "{pedido.periodo.null}")
	@Column(name = "periodo", nullable = false, length = 4)
	private Integer periodo;
	
	@NotNull(message = "{pedido.tipkar.null}")
	@Column(name = "tipkar", nullable = false, length = 1)
	private String tipkar;
	
	@Column(name = "numped", nullable = false, length = 4)
	private Integer numped;
	
	@NotNull(message = "{pedido.fecha.null}")
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@NotNull(message = "{pedido.docref.null}")
	@Size(max=3, message="{pedido.docref.size}")
	@Column(name = "docref", nullable = false, length = 3)
	private String docref;
	
	@NotNull(message = "{pedido.numref.null}")
	@Size(max=20, message="{pedido.numref.size}")
	@Column(name = "numref", nullable = false, length = 20)
	private String numref;
	
	@NotNull(message = "{pedido.fecref.null}")
	@Column(name = "fecref", nullable = false)
	private LocalDate fecref;
	
	@NotNull(message = "{pedido.empleado.null}")
	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false, foreignKey = @ForeignKey(name = "FK_empleado"))
	private Empleado empleado;
	
	@Column(name = "totped", nullable = false)
	private double totped;
	
	@Column(name = "valped", nullable = false)
	private double valped;
	
	@Column(name = "totvta", nullable = false)
	private double totvta;
	
	@Column(name = "observ", nullable = true, length = 100)
	private String observ;
	
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@Transient
	private String desEstado;
	
	@OneToMany(mappedBy = "pedido", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<Itepedido> detpedido;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}
