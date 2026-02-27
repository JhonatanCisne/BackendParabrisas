package com.parabrisas.backend.usuario;

public record LoginResponseDTO(
    String token,

    String mensaje,

    String nombres,

    Long idUsuario,

    String rol,

    Long expiraEn
) {}
