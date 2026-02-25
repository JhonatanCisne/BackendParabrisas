package com.parabrisas.backend.proveedor;

public record ProveedorDTO(
    int idProveedor,
    String nombreProveedor,
    String telefono,
    String direccion,
    String estadoCredito,
    String montoCredito
) {}
