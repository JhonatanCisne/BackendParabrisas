package com.parabrisas.backend.venta;

import com.parabrisas.backend.detalleVenta.DetalleVentaListDTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record VentaDT0(
    Integer idVenta,

    @NotNull(message = "El ID del usuario/vendedor es obligatorio")
    Integer idUsuario,

    LocalDate fecha,

    LocalTime hora,

    @NotNull(message = "El total de la venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a cero")
    BigDecimal totalVenta,

    @NotBlank(message = "La placa del vehículo es obligatoria para la venta")
    String placaVehiculo,

    @NotEmpty(message = "La venta debe tener al menos un producto")
    List<DetalleVentaListDTO> detalles
) {}
