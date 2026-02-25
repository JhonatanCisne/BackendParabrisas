package com.parabrisas.backend.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.parabrisas.backend.detalleCompra.DetalleListCompraDTO;
import jakarta.validation.constraints.*;

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
