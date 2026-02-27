package com.parabrisas.backend.detalleVenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository <DetalleVenta, Long>{
}
