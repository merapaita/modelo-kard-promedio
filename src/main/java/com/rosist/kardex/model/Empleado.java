package com.rosist.kardex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Empleado")		// , uniqueConstraints=@UniqueConstraint(columnNames={"periodo","numii"})
public class Empleado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEmpleado;
	
	@NotNull(message = "{empleado.nombre.null}")
	@Size(min = 3,  message = "{empleado.nombre.size}")
	@Column(name = "nombre", nullable = false, length = 60)
	private String nombre;
	
}
