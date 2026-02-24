package com.parabrisas.backend.proveedor;

import com.parabrisas.backend.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(nullable = false, length = 100)
    private String nombreProveedor;

    @Column(length = 25)
    private String telefono;

    @Column(length = 400)
    private String direccion;

    @Column(length = 30)
    private String estadoCredito;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoCredito;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<Producto> productos;

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstadoCredito() {
        return estadoCredito;
    }

    public void setEstadoCredito(String estadoCredito) {
        this.estadoCredito = estadoCredito;
    }

    public BigDecimal getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(BigDecimal montoCredito) {
        this.montoCredito = montoCredito;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
