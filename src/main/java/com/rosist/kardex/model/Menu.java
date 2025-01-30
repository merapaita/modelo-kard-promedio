//package com.rosist.kardex.model;
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
//import jakarta.persistence.UniqueConstraint;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@ToString
//@Entity
//@Table(name = "menu", uniqueConstraints=@UniqueConstraint(columnNames={"item"}))
//public class Menu {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer idMenu;
//
//	@Column(name = "icono", length = 20)
//	private String icono;
//
//	@Column(name = "nombre", length = 30)
//	private String nombre;
//
//	@Column(name = "url", length = 50)
//	private String url;
//
//	@Column(name = "item", length = 10)
//	private Integer item;
//
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "menu_rol", joinColumns = @JoinColumn(name = "id_menu", referencedColumnName = "idMenu"), inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "idRol"))
//	private List<Rol> roles;
//
//}
