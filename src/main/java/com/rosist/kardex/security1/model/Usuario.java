//package com.rosist.kardex.security1.model;
//
//import java.util.List;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//@Entity
//@Table(name = "usuario")
//@Data
//public class Usuario {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer idUsuario;
//
//	@Column(name = "username", nullable = false, unique = true)
//	private String username;
//
//	@Column(name = "password", nullable = false)
//	private String password;
//	
//	@Column(name = "nombre", nullable = false, unique = true)
//	private String nombre;
//	
//	@Column(name = "email", nullable = true, unique = true)
//	private String email;
//	
//    private String tokenPassword;
//	//SELECT * FROM USUARIO WHERE USERNAME = ?
//	//idUsuario, username, password, enabled, roles [LLENA]
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "idUsuario"), 
//	inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "idRol"))
//	private List<Rol> roles;
//	
//    public Usuario() {
//    }
//
//    public Usuario(@NotNull String nombre, @NotNull String username, @NotNull String email, @NotNull String password) {
//        this.nombre = nombre;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//    }
//
//	
//}