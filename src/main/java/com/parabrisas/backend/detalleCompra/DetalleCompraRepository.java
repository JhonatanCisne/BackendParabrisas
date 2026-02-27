package com.parabrisas.backend.detalleCompra;

import com.parabrisas.backend.compra.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository <DetalleCompra, Long> {
    List<DetalleCompra> findByCompra(Compra compra);
}
