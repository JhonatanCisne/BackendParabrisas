package com.parabrisas.backend.usuario;

import com.parabrisas.backend.security.jwt.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parabrisas.backend.usuario.LoginRequestDTO;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.usuarioRepository=usuarioRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtils=jwtUtils;
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioRepository.findByCorreo(loginRequest.correo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginRequest.contrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtils.generateJwtToken(
                usuario.getIdUsuario(),
                usuario.getCorreo(),
                usuario.getRol()
        );

        return new LoginResponseDTO(
                token,
                "Bienvenido " + usuario.getNombres(),
                usuario.getNombres(),
                usuario.getRol(),
                3600L
        );
    }

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombres(usuarioDTO.nombres());
        usuario.setApellidos(usuarioDTO.apellidos());
        usuario.setCorreo(usuarioDTO.correo());
        usuario.setRol(usuarioDTO.rol());

        String passwordHasheada = passwordEncoder.encode(usuarioDTO.contrasena());
        usuario.setContrasena(passwordHasheada);

        Usuario guardado = usuarioRepository.save(usuario);
        return entityToDto(guardado);
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) {
        Usuario existente = usuarioRepository.findByCorreo(usuarioDTO.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setNombres(usuarioDTO.nombres());
        existente.setApellidos(usuarioDTO.apellidos());

        // Si el DTO trae una contraseña nueva, se hasheamos y actualiza
        if (usuarioDTO.contrasena() != null && !usuarioDTO.contrasena().isBlank()) {
            existente.setContrasena(passwordEncoder.encode(usuarioDTO.contrasena()));
        }

        return entityToDto(usuarioRepository.save(existente));
    }

    @Transactional
    public void eliminarUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public NombreUsuarioDTO obtenerReferenciaUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(u -> new NombreUsuarioDTO(
                        u.getIdUsuario().intValue(),
                        u.getNombres(),
                        u.getApellidos()
                ))
                .orElseThrow(() -> new RuntimeException("ID de usuario no encontrado"));
    }

    private UsuarioDTO entityToDto(Usuario u) {
        return new UsuarioDTO(
                u.getIdUsuario(),
                u.getNombres(),
                u.getApellidos(),
                u.getCorreo(),
                u.getRol(),
                null
        );
    }


}
