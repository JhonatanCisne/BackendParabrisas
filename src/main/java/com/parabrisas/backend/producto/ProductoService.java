package com.parabrisas.backend.producto;


import com.parabrisas.backend.proveedor.Proveedor;
import com.parabrisas.backend.proveedor.ProveedorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductListDTO> listarProductos(FiltroVidrioDTO filtro) {
        Specification<Producto> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (filtro.marcaVehiculo() != null) predicates.add(cb.equal(root.get("marcaVehiculo"), filtro.marcaVehiculo()));
            if (filtro.modeloVehiculo() != null) predicates.add(cb.equal(root.get("modeloVehiculo"), filtro.modeloVehiculo()));
            if (filtro.anioVehiculo() != null) predicates.add(cb.equal(root.get("anioVehiculo"), filtro.anioVehiculo()));
            if (filtro.tipoVidrio() != null) predicates.add(cb.equal(root.get("tipoVidrio"), filtro.tipoVidrio()));
            if (filtro.calidadVidrio() != null) predicates.add(cb.equal(root.get("calidadVidrio"), filtro.calidadVidrio()));

            // Filtro por nombre de proveedor (Join)
            if (filtro.nombreProveedor() != null) {
                predicates.add(cb.equal(root.join("proveedor").get("nombreProveedor"), filtro.nombreProveedor()));
            }

            // Filtro disponibilidad (stock > 0)
            if (filtro.disponible() != null && filtro.disponible()) {
                predicates.add(cb.greaterThan(root.get("stockActual"), 0));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return productoRepository.findAll(spec).stream()
                .map(this::mapToProductListDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductListDTO crearProducto(ProductListDTO dto) {
        Proveedor proveedor = proveedorRepository.findByNombreProveedor(dto.nombreProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Producto producto = new Producto();
        mapDtoToEntity(dto, producto, proveedor);

        return mapToProductListDTO(productoRepository.save(producto));
    }

    @Transactional
    public ProductListDTO actualizarProducto(ProductListDTO dto) {
        Producto existente = productoRepository.findById(dto.idProducto().longValue())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Proveedor proveedor = proveedorRepository.findByNombreProveedor(dto.nombreProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        mapDtoToEntity(dto, existente, proveedor);
        return mapToProductListDTO(productoRepository.save(existente));
    }

    @Transactional
    public void eliminarProducto(int idProducto) {
        productoRepository.deleteById((long) idProducto);
    }

    @Transactional(readOnly = true)
    public ProductListDTO buscarProductoPorId(int idProducto) {
        return productoRepository.findById((long) idProducto)
                .map(this::mapToProductListDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public void descontarStock(int idProducto, int cantidad) {
        Producto producto = productoRepository.findById((long) idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);
    }

    @Transactional
    public void anadirStock(int idProducto, int cantidad) {
        Producto producto = productoRepository.findById((long) idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStockActual(producto.getStockActual() + cantidad);
        productoRepository.save(producto);
    }

    // --- Mapeos ---

    private void mapDtoToEntity(ProductListDTO dto, Producto p, Proveedor prov) {
        p.setMarcaVehiculo(dto.marcaVehiculo());
        p.setModeloVehiculo(dto.modeloVehiculo());
        p.setAnioVehiculo(dto.anioVehiculo());
        p.setCalidadVidrio(dto.calidadVidrio());
        p.setTipoVidrio(dto.tipoVidrio());
        p.setProveedor(prov);
        p.setCostoCompra(dto.costoCompra());
        p.setPrecioVenta(dto.precioVenta());
        p.setStockActual(dto.stockActual());
        p.setUbicacionAlmacen(dto.ubicacionAlmacen());
        if (p.getStockMinimo() == null) p.setStockMinimo(5);
    }

    private ProductListDTO mapToProductListDTO(Producto p) {
        return new ProductListDTO(
                p.getIdProducto().intValue(),
                p.getMarcaVehiculo(),
                p.getModeloVehiculo(),
                p.getAnioVehiculo(),
                p.getCalidadVidrio(),
                p.getTipoVidrio(),
                p.getProveedor().getNombreProveedor(),
                p.getCostoCompra(),
                p.getPrecioVenta(),
                p.getStockActual(),
                p.getUbicacionAlmacen()
        );
    }

    @Transactional(readOnly = true)
    public List<ProductListDTO> buscarProductosPorPlaca(String placa) {
        // Devolver todos los productos disponibles con stock > 0
        return productoRepository.findAll().stream()
                .filter(p -> p.getStockActual() > 0)
                .map(this::mapToProductListDTO)
                .collect(Collectors.toList());
    }
}
