package com.parabrisas.backend.producto;

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

import com.parabrisas.backend.shared.dto.ProductoBajoStockDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/catalogo")
    public ResponseEntity<List<ProductListDTO>> listar(@RequestBody FiltroVidrioDTO filtro) {
        return ResponseEntity.ok(productoService.listarProductos(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductListDTO> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(productoService.buscarProductoPorId(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<ProductListDTO> crear(@Valid @RequestBody ProductListDTO productoDTO) {
        return new ResponseEntity<>(productoService.crearProducto(productoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ProductListDTO> actualizar(@Valid @RequestBody ProductListDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(productoDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock/descontar")
    public ResponseEntity<Void> descontarStock(@PathVariable int id, @RequestParam int cantidad) {
        productoService.descontarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock/anadir")
    public ResponseEntity<Void> anadirStock(@PathVariable int id, @RequestParam int cantidad) {
        productoService.anadirStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock-bajo-alerta")
    public ResponseEntity<Void> toggleStockBajoAlerta(@PathVariable int id, @RequestParam boolean valor) {
        productoService.actualizarStockBajoAlerta(id, valor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/buscar-placa/{placa}")
    public ResponseEntity<List<ProductListDTO>> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(productoService.buscarProductosPorPlaca(placa));
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoBajoStockDTO>> obtenerProductosBajoStock() {
        return ResponseEntity.ok(productoService.obtenerProductosBajoStock());
    }
}
