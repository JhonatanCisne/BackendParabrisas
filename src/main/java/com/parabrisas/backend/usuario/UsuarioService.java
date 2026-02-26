package com.parabrisas.backend.usuario;

import org.aspectj.weaver.patterns.IToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parabrisas.backend.usuario.LoginRequestDTO;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }

    @Transactional (readOnly = true)
    public LoginResponseDTO Login (String correo, String contrasena){
        Usuario usuario= usuarioRepository.findByCorreo(correo).
                orElseThrow(() -> new RuntimeException("Credenciales invalidas"));
        if (!usuario.getContrasena().equals(contrasena)){
            throw new RuntimeException("Credenciales inválidas");
        }
        return new LoginResponseDTO(
                "token generado",
                "Bienvenido",
                usuario.getNombres(),
                usuario.getRol().toString(),
                3600L
        );
    }

    /*+ login(request: LoginRequestDTO): LoginResponseDTO
+ crearUsuario(usuarioDTO: UsuarioDTO): UsuarioDTO
+ actualizarUsuario(usuarioDTO: UsuarioDTO): UsuarioDTO
+ eliminarUsuario(correo: String): void
+ buscarUsuarioPorCorreo(correo: String): Optional<Usuario>
+ obtenerReferenciaUsuario (idUsuario: int): NombreUsuarioDTO */
}
