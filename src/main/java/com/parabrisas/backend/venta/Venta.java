package com.parabrisas.backend.venta;

import com.parabrisas.backend.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fechaVenta;

    @Column(nullable = false)
    private Float totalVenta;

    public Venta() {}

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(Float totalVenta) {
        this.totalVenta = totalVenta;
    }
}
