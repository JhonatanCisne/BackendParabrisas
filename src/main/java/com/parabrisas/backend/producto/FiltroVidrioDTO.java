package com.parabrisas.backend.producto;

import java.math.BigDecimal;

public record FiltroVidrioDTO(
    String marcaVehiculo,

    String modeloVehiculo ,

    String anioVehiculo,

    String tipoVidrio,

    String calidadVidrio,

    String nombreProveedor,

    BigDecimal precioVenta,

    Boolean disponible
){}
