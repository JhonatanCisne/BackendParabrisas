package com.parabrisas.backend.shared.dto;

import java.math.BigDecimal;

public record ProductoBajoStockDTO(
    Long idProducto,
    String marcaVehiculo,
    String modeloVehiculo,
    String anioVehiculo,
    String tipoVidrio,
    String calidadVidrio,
    String nombreProveedor,
    Integer stockActual,
    BigDecimal precioVenta
) {}
