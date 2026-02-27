package com.parabrisas.backend.proveedor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreProveedor(nombre);
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> buscarPorEstadoCredito(String estado) {
        return proveedorRepository.findAllByEstadoCredito(estado)
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        mapDtoToEntity(dto, proveedor);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return mapEntityToDto(guardado);
    }

    @Transactional
    public ProveedorDTO actualizarProveedor(ProveedorDTO dto) {
        Proveedor existente = proveedorRepository.findById((long) dto.idProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + dto.idProveedor()));

        mapDtoToEntity(dto, existente);

        Proveedor actualizado = proveedorRepository.save(existente);
        return mapEntityToDto(actualizado);
    }

    @Transactional
    public void actualizarMontoCredito(String nombreProveedor, BigDecimal montoCredito) {
        Proveedor proveedor = proveedorRepository.findByNombreProveedor(nombreProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + nombreProveedor));

        proveedor.setMontoCredito(montoCredito);
        proveedorRepository.save(proveedor);
    }

    @Transactional
    public void eliminarProveedor(int idProveedor) {
        if (!proveedorRepository.existsById((long) idProveedor)) {
            throw new RuntimeException("No se puede eliminar: El proveedor no existe.");
        }
        proveedorRepository.deleteById((long) idProveedor);
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // Métodos de conversión

    private void mapDtoToEntity(ProveedorDTO dto, Proveedor entidad) {
        entidad.setNombreProveedor(dto.nombreProveedor());
        entidad.setTelefono(dto.telefono());
        entidad.setDireccion(dto.direccion());
        entidad.setEstadoCredito(dto.estadoCredito());
        entidad.setMontoCredito(dto.montoCredito());
    }

    private ProveedorDTO mapEntityToDto(Proveedor entidad) {
        return new ProveedorDTO(
                entidad.getIdProveedor().intValue(),
                entidad.getNombreProveedor(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getEstadoCredito(),
                entidad.getMontoCredito()
        );
    }
}
