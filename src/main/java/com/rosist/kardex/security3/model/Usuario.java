//package com.rosist.kardex.security3.model;
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
//@Entity
//@Table(name = "usuario")
//public class Usuario {
//
//	@Id
//	private Integer idUsuario;
//
//	@Column(name = "username", nullable = false, unique = true)
//	private String username;
//
//	@Column(name = "password", nullable = false)
//	private String password;
//
//	@Column(name = "enabled", nullable = false)
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
//	public Integer getIdUsuario() {
//		return idUsuario;
//	}
//
//	public void setIdUsuario(Integer idUsuario) {
//		this.idUsuario = idUsuario;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public List<Rol> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(List<Rol> roles) {
//		this.roles = roles;
//	}
//
//}
