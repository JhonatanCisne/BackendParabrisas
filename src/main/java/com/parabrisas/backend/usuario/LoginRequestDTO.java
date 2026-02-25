package com.parabrisas.backend.usuario;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
     @NotBlank (message = "El correo no puede estar vacío")
     String correo,

     @NotBlank(message = "La contraseña no puede estar vacía")
     String contrasena
) {}
