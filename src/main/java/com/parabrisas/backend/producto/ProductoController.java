package com.parabrisas.backend.producto;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
