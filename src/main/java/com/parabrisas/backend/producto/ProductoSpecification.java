package com.parabrisas.backend.producto;

import java.math.BigDecimal;

public record ProductoSpecification(
     Integer idProducto,

     String marcaVehiculo,

     String modeloVehiculo,

     String anioVehiculo,

     String calidadVidrio,

     String tipoVidrio,

     String nombreProveedor,

     BigDecimal costoCompra,

     BigDecimal precioVenta,

     Integer stockActual,

     String ubicacionAlmacen
) {}
