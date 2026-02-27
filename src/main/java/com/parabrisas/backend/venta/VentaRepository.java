package com.parabrisas.backend.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository <Venta, Long> {
    Optional<Venta> findByFechaVenta(LocalDate fechaVenta);
    Optional <Venta> findByPlaca (String placa);
}
