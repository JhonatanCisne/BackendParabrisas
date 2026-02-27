package com.parabrisas.backend.detalleCompra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCompraRepository extends JpaRepository <DetalleCompra, Long> {
}
