package com.parabrisas.backend.shared.dto;

import java.math.BigDecimal;

public record EstadisticasDTO(
    String vidrioMasVendido,
    Integer totalVidriosVendidos,
    Integer totalVidriosEnStock,
    BigDecimal totalGeneralVentas
) {}
