package com.parabrisas.backend.proveedor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ProveedorDTO> crear(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        return new ResponseEntity<>(proveedorService.crearProveedor(proveedorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ProveedorDTO> actualizar(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        return ResponseEntity.ok(proveedorService.actualizarProveedor(proveedorDTO));
    }

    @PatchMapping("/actualizar-monto/{nombre}")
    public ResponseEntity<Void> actualizarMonto(
            @PathVariable String nombre,
            @RequestParam BigDecimal monto) {
        proveedorService.actualizarMontoCredito(nombre, monto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proveedor> buscarPorNombre(@PathVariable String nombre) {
        return proveedorService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ProveedorDTO>> buscarPorEstado(@PathVariable String estado) {
        List<ProveedorDTO> proveedores = proveedorService.buscarPorEstadoCredito(estado);
        return ResponseEntity.ok(proveedores);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
