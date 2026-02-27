package com.parabrisas.backend.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository <Compra, Long>{
    Optional<Compra> findByFechaCompra (LocalDate fechaCompra);
}
