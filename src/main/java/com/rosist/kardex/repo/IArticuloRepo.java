package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Articulo;

public interface IArticuloRepo extends IGenericRepo<Articulo, Integer>, JpaSpecificationExecutor<Articulo> {

	@Query(value = "select ifnull(max(id_articulo),0)+1 from articulo", nativeQuery = true)
	Integer getNewIdArticulo();
	
	@Query(value = "select lpad(ifnull(max(substr(codart,9,12)),0)+1,4,'0') from articulo where id_familia=:id_familia", nativeQuery = true)
	String getNewCodart(@Param("id_familia") Integer id_familia);
	
	@Query(value = "select lpad(ifnull(max(familia),0)+1,4,'0') from afamilia where id_clase=:id_clase", nativeQuery = true)
	String getNewClase(@Param("id_clase") Integer id_clase);
	
	@Query(value = "SELECT * FROM articulo WHERE tipo=:tipo and codart=:codart", nativeQuery = true)
	List<Articulo> listaPorCodart(@Param("tipo") String tipo, @Param("codart") String codart);
	
	@Query(value = "SELECT * FROM articulo WHERE tipo=:tipo order by tipo, codart", nativeQuery = true)
	Page<Articulo> listaPorTipo(@Param("tipo") String tipo, Pageable page);
	
	@Query(value = "SELECT * FROM articulo WHERE tipo=:tipo and nomart like %:nomart%", nativeQuery = true)
	List<Articulo> listaPorNomart(@Param("tipo") String tipo, @Param("nomart") String nomart);
	
	@Query(value = "SELECT * FROM articulo WHERE tipo=:tipo and nomart like %:nomart%", nativeQuery = true)
	Page<Articulo> listaPorNomart(@Param("tipo") String tipo, @Param("nomart") String nomart, Pageable page);
	
	@Query(value = "select coalesce(to_number(max(left(codart,8)),'99G999D9S'),0)+1 from articulo where left(codart,7)=:codigo", nativeQuery = true)
	Integer getNewCorrel(@Param("codigo") String codigo);
	
	@Query(value = "call pr_articulo_count(:tipo, :nomart) ", nativeQuery = true)
	Integer countArticulo( 
			@Param("tipo") String tipo, 
			@Param("nomart") String nomart);
	
	@Query(value = "call pr_articulo(:tipo, :nomart, :page, :size) ", nativeQuery = true)
	List<Object[]> listaArticulo(
			@Param("tipo") String tipo, 
			@Param("nomart") String nomart, 
			@Param("page") Integer page, @Param("size") Integer size );

}