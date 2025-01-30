package com.rosist.kardex.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "proveedor")
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProveedor;
	
	@Size(max=60, message="nombre no debe estar vacio")
	@Column(name = "nombre", nullable = false, length = 60)
	private String nombre;
	
	@Size(max=60, message="direccion no debe estar vacio")
	@Column(name = "direccion", nullable = false, length = 60)
	private String direccion;
	
	@Size(max=11, message="ruc no debe estar vacio")
	@Column(name = "ruc", nullable = false, length = 11, unique = true)
	private String ruc;
	
	@Size(max=2, message="estado no debe estar vacio")
	@Column(name = "estado", nullable = false, length = 2)
	private String estado;
	
	@Column(name = "userup", length = 15, nullable = true)
	private String userup;

	@Column(name = "usercr", length = 15, nullable = true)
	private String usercr;
	
	@Column(name = "duserup", nullable = true)
	private LocalDateTime duserup;

	@Column(name = "dusercr", nullable = true)
	private LocalDateTime dusercr;
	
}
