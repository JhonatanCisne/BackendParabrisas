package com.parabrisas.backend.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        Long idUsuario,
        @NotBlank(message = "El nombre es obligatorio")
        String nombres,
        @NotBlank(message = "El apellido es obligatorio")
        String apellidos,
        @Email(message = "Correo inválido")
        String correo,
        String rol,
        String contrasena
) {}
