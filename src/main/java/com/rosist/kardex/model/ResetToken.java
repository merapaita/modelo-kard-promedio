//package com.rosist.kardex.model;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//public class ResetToken {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//
//	@Column(nullable = false, unique = true)
//	private String token;
//	
//	@OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
//	@JoinColumn(nullable = false, name = "id_usuario")
//	private Usuario user;
//
//	@Column(nullable = false)
//	private LocalDateTime expiracion;
//
//	public void setExpiracion(int minutos) {	
//		LocalDateTime hoy = LocalDateTime.now();
//		LocalDateTime exp = hoy.plusMinutes(minutos);
//		this.expiracion = exp;
//	}
//	
//	public boolean estaExpirado() {
//		return LocalDateTime.now().isAfter(this.expiracion);
//	}
//
//}
