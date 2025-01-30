//package com.rosist.kardex.repo;
//
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.rosist.kardex.model.Usuario;
//
//public interface ILoginRepo extends IGenericRepo<Usuario, Integer>{
//
//	@Query("FROM Usuario us where us.username =:usuario")
//	Usuario verificarNombreUsuario(@Param("usuario")String usuario);
//	
//	@Query("FROM Usuario us where us.correo =:correo")
//	Usuario verificarCorreoUsuario(@Param("correo")String correo);
//	
//	@Transactional
//	@Modifying
//	@Query("UPDATE Usuario us SET us.password =:clave WHERE us.username =:nombre")
//	void cambiarClave(@Param("clave") String clave, @Param("nombre") String nombre);
//	
//}
