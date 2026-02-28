package com.parabrisas.backend.producto;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    
	@Query("SELECT p FROM Producto p WHERE p.marcaVehiculo = :marca " +
		   "AND p.modeloVehiculo = :modelo " +
		   "AND p.anioVehiculo = :anio " +
		   "AND p.tipoVidrio = :tipo " +
		   "AND p.calidadVidrio = :calidad " +
		   "AND p.proveedor.idProveedor = :idProveedor")
	List<Producto> findProductosSimilares(
		@Param("marca") String marcaVehiculo,
		@Param("modelo") String modeloVehiculo,
		@Param("anio") String anioVehiculo,
		@Param("tipo") String tipoVidrio,
		@Param("calidad") String calidadVidrio,
		@Param("idProveedor") Long idProveedor
	);
}
