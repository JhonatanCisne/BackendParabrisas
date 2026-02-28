package com.parabrisas.backend.compra;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parabrisas.backend.detalleCompra.DetalleCompra;
import com.parabrisas.backend.detalleCompra.DetalleCompraRepository;
import com.parabrisas.backend.detalleCompra.DetalleListCompraDTO;
import com.parabrisas.backend.producto.Producto;
import com.parabrisas.backend.producto.ProductoRepository;
import com.parabrisas.backend.producto.ProductoService;
import com.parabrisas.backend.proveedor.Proveedor;
import com.parabrisas.backend.proveedor.ProveedorRepository;
import com.parabrisas.backend.usuario.Usuario;
import com.parabrisas.backend.usuario.UsuarioRepository;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public CompraService(CompraRepository compraRepository,
                         DetalleCompraRepository detalleCompraRepository,
                         ProveedorRepository proveedorRepository,
                         UsuarioRepository usuarioRepository,
                         ProductoRepository productoRepository,
                         ProductoService productoService) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @Transactional
    public CompraDTO generarCompra(CompraDTO compraDTO) {
        Proveedor proveedor = proveedorRepository.findByNombreProveedor(compraDTO.nombreProveedor())
                .orElseThrow(() -> new RuntimeException("El proveedor '" + compraDTO.nombreProveedor() + "' no existe"));

        Usuario usuario = usuarioRepository.findById(compraDTO.idUsuario().longValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la cabecera
        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setFechaCompra(LocalDate.now());
        compra.setTotalCompra(compraDTO.totalCompra());

        Compra guardada = compraRepository.save(compra);

        for (DetalleListCompraDTO dDto : compraDTO.detalle()) {
            Producto producto;

            // Si idProducto es 0 o nulo, buscar/crear el producto basado en detalles
            if (dDto.idProducto() == null || dDto.idProducto() == 0) {
                // Buscar producto con los datos disponibles
                producto = productoRepository.findAll().stream()
                        .filter(p -> p.getMarcaVehiculo().equalsIgnoreCase(dDto.marcaVehiculo()))
                        .filter(p -> p.getModeloVehiculo().equalsIgnoreCase(dDto.modeloVehiculo()))
                        .filter(p -> p.getAnioVehiculo().equals(dDto.anioVehiculo()))
                        .filter(p -> p.getTipoVidrio().equals(dDto.tipoVidrio()))
                        .filter(p -> p.getCalidadVidrio().equals(dDto.calidadVidrio()))
                        .findFirst()
                        .orElse(null);

                if (producto == null) {
                    // Crear nuevo producto
                    producto = new Producto();
                    producto.setMarcaVehiculo(dDto.marcaVehiculo());
                    producto.setModeloVehiculo(dDto.modeloVehiculo());
                    producto.setAnioVehiculo(dDto.anioVehiculo());
                    producto.setTipoVidrio(dDto.tipoVidrio());
                    producto.setCalidadVidrio(dDto.calidadVidrio());

                    // Obtener proveedor desde idProveedor si viene en detalle
                    if (dDto.idProveedor() != null && dDto.idProveedor() > 0) {
                        Proveedor proveedorProducto = proveedorRepository.findById(dDto.idProveedor().longValue())
                                .orElse(proveedor);
                        producto.setProveedor(proveedorProducto);
                    } else {
                        producto.setProveedor(proveedor);
                    }

                    producto.setCostoCompra(dDto.costoCompra());
                    producto.setPrecioVenta(dDto.precioVenta());
                    producto.setStockActual(0);
                    producto.setStockBajoAlerta(true);
                    producto.setUbicacionAlmacen(dDto.ubicacionAlmacen());

                    producto = productoRepository.save(producto);
                } else {
                    // Actualizar datos del producto existente con la nueva compra
                    producto.setCostoCompra(dDto.costoCompra());
                    producto.setPrecioVenta(dDto.precioVenta());
                    producto.setUbicacionAlmacen(dDto.ubicacionAlmacen());
                    producto = productoRepository.save(producto);
                }
            } else {
                // Usar producto existente por ID
                producto = productoRepository.findById(dDto.idProducto().longValue())
                        .orElseThrow(() -> new RuntimeException("Producto ID " + dDto.idProducto() + " no encontrado"));
            }

            // Crear detalle de compra
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(guardada);
            detalle.setProducto(producto);
            detalle.setCantidad(dDto.cantidad());
            detalle.setCostoCompra(dDto.costoCompra());
            detalle.setPrecioVenta(dDto.precioVenta());
            detalle.setUbicacionAlmacen(dDto.ubicacionAlmacen());
            detalleCompraRepository.save(detalle);

            // Incrementar stock
            productoService.anadirStock(producto.getIdProducto().intValue(), dDto.cantidad());
        }

        return mapToCompraDTO(guardada, compraDTO.detalle());
    }

    @Transactional(readOnly = true)
    public List<CompraDTO> listarCompras() {
        return compraRepository.findAll().stream()
                .map(compra -> {
                    List<DetalleListCompraDTO> detalles = detalleCompraRepository.findByCompra(compra)
                            .stream().map(this::mapToDetalleDTO).collect(Collectors.toList());
                    return mapToCompraDTO(compra, detalles);
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<List<CompraDTO>> buscarCompraPorFecha(LocalDate fecha) {
        List<Compra> compras = compraRepository.findAll().stream()
                .filter(c -> c.getFechaCompra().equals(fecha))
                .collect(Collectors.toList());

        if (compras.isEmpty()) return Optional.empty();

        List<CompraDTO> dtos = compras.stream().map(c -> {
            List<DetalleListCompraDTO> detalles = detalleCompraRepository.findByCompra(c)
                    .stream().map(this::mapToDetalleDTO).collect(Collectors.toList());
            return mapToCompraDTO(c, detalles);
        }).collect(Collectors.toList());

        return Optional.of(dtos);
    }

    @Transactional(readOnly = true)
    public List<CompraDTO> buscarCompraPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Compra> compras = compraRepository.findAll().stream()
                .filter(c -> !c.getFechaCompra().isBefore(fechaInicio) && !c.getFechaCompra().isAfter(fechaFin))
                .collect(Collectors.toList());

        return compras.stream().map(c -> {
            List<DetalleListCompraDTO> detalles = detalleCompraRepository.findByCompra(c)
                    .stream().map(this::mapToDetalleDTO).collect(Collectors.toList());
            return mapToCompraDTO(c, detalles);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CompraDTO> buscarCompraPorId(Long idCompra) {
        return compraRepository.findById(idCompra).map(compra -> {
            List<DetalleListCompraDTO> detalles = detalleCompraRepository.findByCompra(compra)
                    .stream().map(this::mapToDetalleDTO).collect(Collectors.toList());
            return mapToCompraDTO(compra, detalles);
        });
    }

    @Transactional(readOnly = true)
    public String obtenerNombreUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById((long) idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getNombres() + " " + usuario.getApellidos();
    }

    private DetalleListCompraDTO mapToDetalleDTO(DetalleCompra d) {
        Producto p = d.getProducto();
        return new DetalleListCompraDTO(
                d.getIdDetalleCompra().intValue(),
                d.getCompra().getProveedor().getIdProveedor().intValue(),
                p.getIdProducto().intValue(),
                p.getMarcaVehiculo(),
                p.getModeloVehiculo(),
                p.getAnioVehiculo(),
                p.getCalidadVidrio(),
                p.getTipoVidrio(),
                d.getCostoCompra() != null ? d.getCostoCompra() : p.getCostoCompra(),
                d.getPrecioVenta() != null ? d.getPrecioVenta() : p.getPrecioVenta(),
                d.getCantidad(),
                d.getUbicacionAlmacen()
        );
    }

    private CompraDTO mapToCompraDTO(Compra c, List<DetalleListCompraDTO> detalles) {
        return new CompraDTO(
                c.getIdCompra().intValue(),
                c.getProveedor().getNombreProveedor(),
                c.getUsuario().getIdUsuario().intValue(),
                c.getFechaCompra(),
                c.getTotalCompra(),
                detalles
        );
    }
}
