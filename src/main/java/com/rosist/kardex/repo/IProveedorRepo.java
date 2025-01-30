package com.rosist.kardex.repo;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Proveedor;

public interface IProveedorRepo extends IGenericRepo<Proveedor, Integer> {

	@Query(value = "SELECT * FROM proveedor WHERE ruc=:ruc", nativeQuery = true)
	Proveedor buscaPorRUC(@Param("ruc") String ruc);
	
	@Query(value = "SELECT * FROM proveedor WHERE nombre like %:nombre%", nativeQuery = true)
	Page<Proveedor> listaPorNombre(@Param("nombre") String nombre, Pageable pageable);
	
	@Modifying
	@Query("UPDATE Proveedor p SET p.nombre=:nombre, p.direccion=:direccion, p.usercr=:usercr, p.dusercr=:dusercr WHERE p.idProveedor =:idProveedor")
	void modificaProveedor(@Param("nombre") String nombre, @Param("direccion") String direccion, @Param("usercr") String usercr, @Param("dusercr") LocalDateTime dusercr, @Param("idProveedor") Integer idProveedor );
	
}
