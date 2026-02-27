package com.parabrisas.backend.compra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

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

    @GetMapping("/buscar-rango-fechas")
    public ResponseEntity<List<CompraDTO>> buscarPorRangoFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(compraService.buscarCompraPorRangoFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<CompraDTO> buscarPorId(@PathVariable Long id) {
        return compraService.buscarCompraPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario-nombre/{id}")
    public ResponseEntity<String> obtenerNombreUsuario(@PathVariable int id) {
        return ResponseEntity.ok(compraService.obtenerNombreUsuario(id));
    }
}
