package com.rosist.kardex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "parmae", uniqueConstraints=@UniqueConstraint(columnNames={"tipo","codigo"}))
public class Parmae {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idParmae;
	
	@Column(name = "tipo", length = 6, nullable = true)
	private String tipo;

	@Column(name = "codigo", length = 6, nullable = true)
	private String codigo;
	
	@Column(name = "descri", length = 100, nullable = true)
	private String descri;
	
}
