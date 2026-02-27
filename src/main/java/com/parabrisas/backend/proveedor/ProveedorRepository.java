package com.parabrisas.backend.proveedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository <Proveedor, Long>{
    Optional <Proveedor> findByNombreProveedor (String nombreProveedor);
    Optional <Proveedor> findByEstadoCredito (String estadoCredito);
}
