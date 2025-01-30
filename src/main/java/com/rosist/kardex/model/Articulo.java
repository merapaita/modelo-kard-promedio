package com.rosist.kardex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.Min;
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
@Table(name = "articulo", uniqueConstraints=@UniqueConstraint(columnNames={"tipo","codart"}))
public class Articulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idArticulo;

	@NotNull(message = "{articulo.tipo.null}")
	@Size(min = 1, max = 1, message = "{articulo.tipo.size}")
	@Column(name = "tipo", nullable = false, length = 1)
	private String tipo;
	
	@Column(name = "codart", nullable = false, length = 12)
    private String codart;

	@NotNull(message="{articulo.afamilia.null}")
	@ManyToOne
	@JoinColumn(name = "id_familia", nullable = false, foreignKey = @ForeignKey(name = "FK_familia"))
	private Afamilia afamilia;

	@NotNull(message="{articulo.nomart.null}")
	@Size(min=1, max=200, message="{articulo.nomart.size}")
	@Column(name = "nomart", nullable = false, length = 200)
	private String nomart;
	
	@NotNull(message="{articulo.menudeo.null}")
	@Column(name = "menudeo", nullable = false)
	private Boolean menudeo;
	
	@NotNull(message="{articulo.fraccion.null}")
	@Min(value = 1, message = "{articulo.fraccion.size}")
	@Column(name = "fraccion", nullable = false, length = 4)
	private Integer fraccion;
	
	@NotNull(message="{articulo.unidad.null}")
	@Column(name = "unidad", nullable = false, length = 10)
	private String unidad;
	
	@Column(name = "unimen", nullable = true, length = 10)
	private String unimen;

}
