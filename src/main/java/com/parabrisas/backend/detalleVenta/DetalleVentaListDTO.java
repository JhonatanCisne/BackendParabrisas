package com.parabrisas.backend.detalleVenta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record DetalleVentaListDTO(
        Integer idDetalleVenta,

        @NotNull(message = "El ID del producto es obligatorio para vincular la venta")
        Integer idProducto,

        LocalDate fecha,

        LocalTime hora,

        @NotBlank(message = "La marca del vehículo no puede ser vacío")
        String marcaVehiculo,

        @NotBlank(message = "El modelo del vehículo no puede ser vacío")
        String modeloVehiculo,

        @NotBlank(message = "El año del vehículo no puede ser vacío")
        String anioVehiculo,

        @NotBlank(message = "La calidad del vidrio del vehículo no puede ser vacío " +
                "(debe ser laminado o templado)")
        String calidadVidrio,

        @NotBlank(message = "El tipo de vidrio del vehículo no puede ser vacío " +
                "(debe ser, ejm:parabrisa delantero, vidrio, posterior, vidrio de puerta delantero, vidrio de puerta posterior, o vidrio fijo delantero, etc... )")
        String tipoVidrio,

        Integer idProveedor,

        @NotNull(message = "El costo es obligatorio")
        @Positive(message = "El valor debe ser mayor a 0")
        @DecimalMin(value = "0.01")
        BigDecimal precioVenta,

        @Positive(message = "El valor debe ser mayor a 0")
        Integer cantidad,

        @NotBlank(message = "La placa del vehiculo es obligatorio")
        String placaVehiculo
) {
}
