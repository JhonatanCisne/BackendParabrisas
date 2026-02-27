package com.parabrisas.backend.detalleCompra;

import com.parabrisas.backend.compra.Compra;
import com.parabrisas.backend.producto.Producto;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    public DetalleCompra() {}

    public Long getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
