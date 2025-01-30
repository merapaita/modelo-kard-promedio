package com.rosist.kardex.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "invini", uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numii"}))
public class Invini {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idInvini;
	
	@NotNull(message = "{invini.periodo.null}")
	@Column(name = "periodo", nullable = false, length = 4)
	private Integer periodo;
	
	@Column(name = "numii", nullable = false, length = 4)
	private Integer numii;
	
	@NotNull(message = "{invini.fecha.null}")
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@NotNull(message = "{invini.docref.null}")
	@Size(max=3, message="{invini.docref.size}")
	@Column(name = "docref", nullable = false, length = 3)
	private String docref;
	
	@NotNull(message = "{invini.numref.null}")
	@Size(min = 1, max=20, message="´{invini.numref.size}")
	@Column(name = "numref", nullable = false, length = 20)
	private String numref;
	
	@NotNull(message = "{invini.fecref.null}")
	@Column(name = "fecref", nullable = false)
	private String fecref;
	
	@Column(name = "observ", nullable = true, length = 100)
	private String observ;
	
	@NotNull(message = "{invini.totii.null}")
	@Column(name = "totii", nullable = false)
	private double totii;
	
	@NotNull(message = "{invini.totval.null}")
	@Column(name = "totval", nullable = false)
	private double totval;
	
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@OneToMany(mappedBy = "invini", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<Iteii> detii;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;
	
	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;
	
	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;
	
}