package com.parabrisas.backend.venta;


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
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/generar")
    public ResponseEntity<VentaDT0> generarVenta(@Valid @RequestBody VentaDT0 ventaDTO) {
        VentaDT0 nuevaVenta = ventaService.generarVenta(
                ventaDTO.detalles(),
                ventaDTO.idUsuario(),
                ventaDTO.placaVehiculo()
        );
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<VentaDT0>> listar() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/buscar-fecha")
    public ResponseEntity<List<VentaDT0>> buscarPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ventaService.buscarVentaPorFecha(fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-rango-fechas")
    public ResponseEntity<List<VentaDT0>> buscarPorRangoFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ventaService.buscarVentaPorRangoFechas(fechaInicio, fechaFin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-placa/{placa}")
    public ResponseEntity<List<VentaDT0>> buscarPorPlaca(@PathVariable String placa) {
        return ventaService.buscarVentaPorPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<VentaDT0> buscarPorId(@PathVariable Long id) {
        return ventaService.buscarVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario-nombre/{id}")
    public ResponseEntity<String> obtenerNombreUsuario(@PathVariable int id) {
        return ResponseEntity.ok(ventaService.obtenerNombreUsuario(id));
    }
}
