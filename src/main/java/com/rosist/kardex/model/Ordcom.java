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
@Table(name = "ordcom", uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numoc"}))
public class Ordcom {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idOrdcom;
	
	@NotNull(message = "{ordcom.periodo.null}")
	@Column(name = "periodo", nullable = false, length = 4)
	private Integer periodo;
	
	@Column(name = "numoc", nullable = false, length = 4)
	private Integer numoc;
	
	@NotNull(message = "{ordcom.fecha.null}")
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@NotNull(message = "{ordcom.docref.null}")
	@Size(max=3, message="{ordcom.docref.size}")
	@Column(name = "docref", nullable = false, length = 3)
	private String docref;
	
	@NotNull(message = "{ordcom.numref.null}")
	@Size(max=20, message="{ordcom.numref.size}")
	@Column(name = "numref", nullable = false, length = 20)
	private String numref;
	
	@NotNull(message = "{ordcom.fecref.null}")
	@Column(name = "fecref", nullable = false)
	private LocalDate fecref;
	
	@NotNull(message = "{ordcom.proveedor.null}")
	@ManyToOne
	@JoinColumn(name = "id_proveedor", nullable = false, foreignKey = @ForeignKey(name = "FK_proveedor"))
	private Proveedor proveedor;
	
	@Column(name = "destino", nullable = true, length = 100)
	private String destino;
	
	@Column(name = "gigv", nullable = false)
	private boolean gigv;
	
	@NotNull(message = "{ordcom.totoc.null}")
	@Column(name = "totoc", nullable = false)
	private double totoc;
	
	@Column(name = "igv", nullable = false)
	private double igv;
	
	@Column(name = "observ", nullable = true, length = 100)
	private String observ;
	
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@OneToMany(mappedBy = "ordcom", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<Iteoc> detordcom;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;

}