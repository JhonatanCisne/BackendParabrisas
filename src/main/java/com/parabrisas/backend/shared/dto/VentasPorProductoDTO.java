package com.parabrisas.backend.shared.dto;

public record VentasPorProductoDTO(
    String marcaVehiculo,
    String modeloVehiculo,
    String anioVehiculo,
    String tipoVidrio,
    String calidadVidrio,
    Integer cantidad
) {}
