package com.parabrisas.backend.detalleCompra;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DetalleListCompraDTO(
        Integer idDetallaCompra,

        Integer idProveedor,

        Integer idProducto,

        String marcaVehiculo,

        String modeloVehiculo,

        String anioVehiculo,

        String calidadVidrio,

        String tipoVidrio,

        @NotNull(message = "El costo es obligatorio")
        @Positive(message = "El valor debe ser mayor a 0")
        @DecimalMin(value = "0.01")
        BigDecimal costoCompra,

        BigDecimal precioVenta,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "El valor debe ser mayor a 0")
        Integer cantidad,

        @NotBlank(message = "La ubicación del almacén es obligatoria")
        String ubicacionAlmacen
) {}
