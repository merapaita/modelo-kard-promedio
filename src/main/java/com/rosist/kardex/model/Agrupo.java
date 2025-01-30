package com.rosist.kardex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "agrupo", uniqueConstraints = @UniqueConstraint(columnNames = { "tipo", "grupo" }))
public class Agrupo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer idGrupo;
	
	@NotNull(message = "{agrupo.tipo.null}")
	@Size(min = 1, max = 1, message = "{agrupo.tipo.size}")
	@Column(name = "tipo", nullable = false, length = 1)
	private String tipo;

	@NotNull(message = "{agrupo.grupo.null}")
	@Size(min = 2, max = 2, message = "{agrupo.tipo.size}")
	@Column(name = "grupo", nullable = false, length = 2)
	private String grupo;
	
	@NotNull(message = "{agrupo.descri.null}")
	@Size(min = 1, max = 100, message = "{agrupo.descri.size}")
	@Column(name = "descri", nullable = false, length = 100)
	private String descri;

}
