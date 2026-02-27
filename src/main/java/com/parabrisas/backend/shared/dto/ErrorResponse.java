package com.parabrisas.backend.shared.dto;

public record ErrorResponse(
        int status,
        String mensaje,
        long timestamp
) {}
