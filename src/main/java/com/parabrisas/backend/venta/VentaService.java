package com.parabrisas.backend.venta;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parabrisas.backend.detalleVenta.DetalleVenta;
import com.parabrisas.backend.detalleVenta.DetalleVentaListDTO;
import com.parabrisas.backend.detalleVenta.DetalleVentaRepository;
import com.parabrisas.backend.producto.Producto;
import com.parabrisas.backend.producto.ProductoRepository;
import com.parabrisas.backend.producto.ProductoService;
import com.parabrisas.backend.usuario.Usuario;
import com.parabrisas.backend.usuario.UsuarioRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public VentaService(VentaRepository ventaRepository,
                        DetalleVentaRepository detalleVentaRepository,
                        UsuarioRepository usuarioRepository,
                        ProductoRepository productoRepository,
                        ProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @Transactional
    public VentaDT0 generarVenta(List<DetalleVentaListDTO> detallesDto, int idUsuario, String placa) {
        Usuario usuario = usuarioRepository.findById((long) idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario vendedor no encontrado"));

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDate.now());

        java.math.BigDecimal total = detallesDto.stream()
                .map(d -> d.precioVenta().multiply(new java.math.BigDecimal(d.cantidad())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        venta.setTotalVenta(total);
        venta.setPlaca(placa);

        Venta ventaGuardada = ventaRepository.save(venta);

        List<DetalleVentaListDTO> detallesGuardados = new ArrayList<>();

        for (DetalleVentaListDTO dDto : detallesDto) {
            Producto producto = productoRepository.findById(dDto.idProducto().longValue())
                    .orElseThrow(() -> new RuntimeException("Producto ID " + dDto.idProducto() + " no existe"));

            productoService.descontarStock(producto.getIdProducto().intValue(), dDto.cantidad());

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(dDto.cantidad());
            detalleVentaRepository.save(detalle);

            detallesGuardados.add(mapToDetalleDTO(detalle, placa));
        }

        return mapToVentaDTO(ventaGuardada, detallesGuardados, placa);
    }

    @Transactional(readOnly = true)
    public List<VentaDT0> listarVentas() {
        return ventaRepository.findAll().stream()
                .map(v -> {
                    List<DetalleVentaListDTO> detalles = detalleVentaRepository.findByVenta(v)
                            .stream().map(d -> mapToDetalleDTO(d, v.getPlaca())).collect(Collectors.toList());
                    return mapToVentaDTO(v, detalles, v.getPlaca());
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<List<VentaDT0>> buscarVentaPorFecha(LocalDate fecha) {
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getFechaVenta().equals(fecha))
                .collect(Collectors.toList());

        if (ventas.isEmpty()) return Optional.empty();

        return Optional.of(ventas.stream().map(v -> {
            List<DetalleVentaListDTO> detalles = detalleVentaRepository.findByVenta(v)
                    .stream().map(d -> mapToDetalleDTO(d, v.getPlaca())).collect(Collectors.toList());
            return mapToVentaDTO(v, detalles, v.getPlaca());
        }).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Optional<List<VentaDT0>> buscarVentaPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> !v.getFechaVenta().isBefore(fechaInicio) && !v.getFechaVenta().isAfter(fechaFin))
                .collect(Collectors.toList());

        if (ventas.isEmpty()) return Optional.empty();

        return Optional.of(ventas.stream().map(v -> {
            List<DetalleVentaListDTO> detalles = detalleVentaRepository.findByVenta(v)
                    .stream().map(d -> mapToDetalleDTO(d, v.getPlaca())).collect(Collectors.toList());
            return mapToVentaDTO(v, detalles, v.getPlaca());
        }).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Optional<List<VentaDT0>> buscarVentaPorPlaca(String placa) {
        // Usando el método del repositorio que definiste
        return ventaRepository.findByPlaca(placa).map(v -> {
            List<DetalleVentaListDTO> detalles = detalleVentaRepository.findByVenta(v)
                    .stream().map(d -> mapToDetalleDTO(d, placa)).collect(Collectors.toList());
            return List.of(mapToVentaDTO(v, detalles, placa));
        });
    }

    @Transactional(readOnly = true)
    public Optional<VentaDT0> buscarVentaPorId(Long idVenta) {
        return ventaRepository.findById(idVenta).map(venta -> {
            List<DetalleVentaListDTO> detalles = detalleVentaRepository.findByVenta(venta)
                    .stream().map(d -> mapToDetalleDTO(d, venta.getPlaca())).collect(Collectors.toList());
            return mapToVentaDTO(venta, detalles, venta.getPlaca());
        });
    }

    @Transactional(readOnly = true)
    public String obtenerNombreUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById((long) idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getNombres() + " " + usuario.getApellidos();
    }

    private DetalleVentaListDTO mapToDetalleDTO(DetalleVenta d, String placa) {
        Producto p = d.getProducto();
        return new DetalleVentaListDTO(
                d.getIdDetalleVenta().intValue(),
                p.getIdProducto().intValue(),
                d.getVenta().getFechaVenta(),
                java.time.LocalTime.now(), // Hora actual
                p.getMarcaVehiculo(),
                p.getModeloVehiculo(),
                p.getAnioVehiculo(),
                p.getCalidadVidrio(),
                p.getTipoVidrio(),
                p.getProveedor().getIdProveedor().intValue(),
                p.getPrecioVenta(),
                d.getCantidad(),
                placa
        );
    }

    private VentaDT0 mapToVentaDTO(Venta v, List<DetalleVentaListDTO> detalles, String placa) {
        return new VentaDT0(
                v.getIdVenta().intValue(),
                v.getUsuario().getIdUsuario().intValue(),
                v.getFechaVenta(),
                java.time.LocalTime.now(),
                v.getTotalVenta(),
                placa,
                detalles
        );
    }
}
