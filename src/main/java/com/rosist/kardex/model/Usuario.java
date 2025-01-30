//package com.rosist.kardex.model;
//
//import java.util.List;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.Table;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@Entity
//@ToString
//@Table(name = "usuario")
//public class Usuario {
//
//	@Id
//	private Integer idUsuario;
//
//	@Column(name = "nombre", nullable = false, unique = true)
//	private String username;
//
//	@Column(name = "clave", nullable = false)
//	private String password;
//	
//	@Column(name = "correo", nullable = true, length = 50)
//	private String correo;
//
//	@Column(name = "estado", nullable = false)
//	private boolean enabled;
//	
//	//SELECT * FROM USUARIO WHERE USERNAME = ?
//	//idUsuario, username, password, enabled, roles [LLENA]
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "idUsuario"), 
//	inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "idRol"))
//	private List<Rol> roles;
//
//}
