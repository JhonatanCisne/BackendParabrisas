package com.parabrisas.backend.proveedor;

import java.math.BigDecimal;

public record ProveedorDTO(
    int idProveedor,
    String nombreProveedor,
    String telefono,
    String direccion,
    String estadoCredito,
    BigDecimal montoCredito
) {}
