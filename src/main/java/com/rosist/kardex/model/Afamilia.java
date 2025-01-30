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
@Table(name = "afamilia", uniqueConstraints=@UniqueConstraint(columnNames={"id_clase","familia"}))
public class Afamilia {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idFamilia;
	
	@NotNull(message="{afamilia.familia.null}")
	@Size(min=4, max=4, message="{afamilia.familia.size}")
	@Column(name = "familia", nullable = false, length = 4)
    private String familia;

	@NotNull(message="{afamilia.descri.null}")
	@Size(min=1, max=100, message="{afamilia.descri.size}")
	@Column(name = "descri", nullable = false, length = 100)
    private String descri;
	
	@NotNull(message="{afamilia.aclase.null}")
	@ManyToOne
	@JoinColumn(name = "id_clase", nullable = false, foreignKey = @ForeignKey(name = "FK_aclase"))
	private Aclase aclase;

}
