package com.rosist.kardex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "aclase", uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo", "clase" }))
public class Aclase {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idClase;

	@NotNull(message = "{aclase.clase.null}")
	@Size(min = 2, max = 2, message = "{aclase.clase.size}")
	@Column(name = "clase", nullable = false, length = 2)
	private String clase;
	
	@NotNull(message = "{aclase.descri.null}")
	@Size(min = 1, max = 100, message = "{aclase.descri.size}")
	@Column(name = "descri", nullable = false, length = 100)
	private String descri;

	@NotNull(message = "{aclase.agrupo.null}")
	@ManyToOne
	@JoinColumn(name = "id_grupo", nullable = false, foreignKey = @ForeignKey(name = "FK_agrupo"))
	private Agrupo agrupo;

}
