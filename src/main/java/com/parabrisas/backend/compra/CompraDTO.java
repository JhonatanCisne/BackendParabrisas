package com.parabrisas.backend.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parabrisas.backend.detalleCompra.DetalleListCompraDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompraDTO(
        Integer idCompra,

        @NotBlank(message = "El nombre del proveedor es obligatorio")
        String nombreProveedor,

        Integer idUsuario,

        LocalDate fechaCompra,

        @Positive(message = "El valor debe ser mayor a 0")
        @DecimalMin(value = "0.01")
        BigDecimal totalCompra,

        @NotEmpty(message = "La compra debe tener al menos un detalle (producto)")
        List<DetalleListCompraDTO> detalle
) {}
