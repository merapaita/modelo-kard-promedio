//package com.rosist.kardex.repo;
//
//import org.springframework.data.jpa.repository.Query;
//
//import com.rosist.kardex.model.Usuario;
//
//public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {
//
//	@Query(value = "select coalesce(max(id_usuario),0)+1 from usuario", nativeQuery = true)
//	Integer getNewId();
//	
//	//from usuario where username = ?
//	Usuario findOneByUsername(String username);
//
//}
