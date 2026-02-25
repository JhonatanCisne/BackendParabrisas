package com.parabrisas.backend.usuario;

public record LoginResponseDTO(
    String token,

    String mensaje,

    String nombres,

    String rol,

    Long expiraEn
) {}
