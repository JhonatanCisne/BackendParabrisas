package com.parabrisas.backend.proveedor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

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

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listar() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }
}
