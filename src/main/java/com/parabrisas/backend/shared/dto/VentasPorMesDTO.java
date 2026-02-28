package com.parabrisas.backend.shared.dto;

import java.math.BigDecimal;

public record VentasPorMesDTO(
    String mes,
    Integer cantidad,
    BigDecimal totalVentas
) {}
