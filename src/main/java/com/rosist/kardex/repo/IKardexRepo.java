package com.rosist.kardex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rosist.kardex.model.Kardex;
import com.rosist.kardex.model.Stock;

public interface IKardexRepo extends IGenericRepo<Kardex, Integer> {

	@Query(value = "select ifnull(max(correl),0)+1 " + "       from kardex k "
			+ "      where k.periodo=:periodo and k.tipkar=:tipkar and k.id_articulo=:id_articulo ", nativeQuery = true)
	Integer getNewCorrel(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("id_articulo") Integer id_articulo);

	@Query(value = "select k "
			+ "       from Kardex k "
			+ "      where k.periodo=:periodo and k.tipkar=:tipkar and k.articulo.idArticulo=:id_articulo"
			+ "      order by k.periodo, k.tipkar, k.articulo.idArticulo, k.correl")
	List<Kardex> kardexPorarticulo(@Param("periodo") Integer periodo, 
			@Param("tipkar") String tipkar,
			@Param("id_articulo") Integer id_articulo);

	@Query(value = "SELECT MAX(correl) "
			+ "       FROM kardex "
			+ "      WHERE periodo=:periodo AND tipkar=:tipkar AND id_articulo=:idArticulo", nativeQuery = true)
	Integer getUltimoCorrel(
			@Param("periodo") Integer periodo, 
			@Param("tipkar") String tipkar,
			@Param("idArticulo") Integer idArticulo);

	@Query(value = "CALL pr_stock_v1(:periodo, :tipkar, :descri, :mes)", nativeQuery = true)
	List<Object[]> getStock(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("descri") String descri, @Param("mes") Integer mes);

	@Query(value = "CALL pr_stock_count(:periodo, :tipkar, :descri, :mes)", nativeQuery = true)
	Integer getStockCount(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("descri") String descri, @Param("mes") Integer mes);

	@Query(value = "CALL pr_kardex_v1(:periodo, :tipkar, :idArticulo, :correl)", nativeQuery = true)
	List<Object[]> getKardex(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("idArticulo") Integer idArticulo, @Param("correl") Integer correl);

	@Query(value = "CALL pr_kardex_count(:periodo, :tipkar, :idArticulo, :correl)", nativeQuery = true)
	Integer getKardexCount(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("idArticulo") Integer idArticulo, @Param("correl") Integer correl);

	@Query(value = "SELECT k.id_kardex, k.periodo, k.tipkar, k.id_articulo, a.codart, a.nomart, a.menudeo menart, "
			+ "       a.fraccion fracart, a.unidad uniart, a.unimen unimenart, k.correl,  k.coraux, "
			+ "       k.estado, k.fecha, k.tipdoc, k.menudeo, k.numdoc, k.item, "
			+ "       k.cantidad, k.fraccion, k.totcan, k.saldo_cantidad, k.tipmov, k.preuni, k.igv, k.valuni, k.preunifr, "
			+ "       k.igvfr, k.valunifr, k.dusercr, k.duserup, k.usercr, k.userup, k.unidad, k.unimen"
			+ "  FROM kardex k     LEFT JOIN articulo a  ON a.id_articulo=k.id_articulo "
			+ " WHERE k.periodo=:periodo AND k.tipkar=:tipkar AND k.id_articulo=:id_articulo AND k.correl=:correl AND k.estado<>'99' "
			+ " ORDER BY k.periodo, k.tipkar, k.id_articulo, k.correl ", nativeQuery = true)
	List<Kardex> getListaKardexPorStock(@Param("periodo") Integer periodo, @Param("tipkar") String tipkar,
			@Param("id_articulo") Integer id_articulo, @Param("correl") Integer correl);

	@Query(value = "select * " + "       from kardex k "
			+ "      where k.tipdoc=:tipdoc and k.periodo=:periodo and k.numdoc=:numdoc and k.item=:item ", nativeQuery = true)
	Kardex getBuscaPorItem(@Param("tipdoc") String tipdoc, @Param("periodo") Integer periodo,
			@Param("numdoc") Integer numdoc, @Param("item") Integer item); // , @Param("preuni") Double preuni

	@Modifying
	@Query(value = "update kardex set estado='99' where tipdoc=:tipdoc and periodo=:periodo and numdoc=:numdoc", nativeQuery = true)
	Integer anula(@Param("tipdoc") String tipdoc, @Param("periodo") Integer periodo, @Param("numdoc") Integer numdoc);

}