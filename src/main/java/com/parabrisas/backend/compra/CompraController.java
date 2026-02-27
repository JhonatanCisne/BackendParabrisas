package com.parabrisas.backend.compra;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/generar")
    public ResponseEntity<CompraDTO> generarCompra(@Valid @RequestBody CompraDTO compraDTO) {
        CompraDTO nuevaCompra = compraService.generarCompra(compraDTO);
        return new ResponseEntity<>(nuevaCompra, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CompraDTO>> listar() {
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/buscar-fecha")
    public ResponseEntity<List<CompraDTO>> buscarPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return compraService.buscarCompraPorFecha(fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario-nombre/{id}")
    public ResponseEntity<String> obtenerNombreUsuario(@PathVariable int id) {
        return ResponseEntity.ok(compraService.obtenerNombreUsuario(id));
    }
}
