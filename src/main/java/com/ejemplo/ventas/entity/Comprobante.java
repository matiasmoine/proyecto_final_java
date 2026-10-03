package com.ejemplo.ventas.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comprobanteId;

    private LocalDateTime fecha;
    private BigDecimal total;
    private Integer cantidadTotalProductos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaComprobante> lineas = new ArrayList<>();

    protected Comprobante() {
    }

    public Comprobante(LocalDateTime fecha, BigDecimal total, Integer cantidadTotalProductos, Cliente cliente) {
        this.fecha = fecha;
        this.total = total;
        this.cantidadTotalProductos = cantidadTotalProductos;
        this.cliente = cliente;
    }

    public Long getComprobanteId() {
        return comprobanteId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getCantidadTotalProductos() {
        return cantidadTotalProductos;
    }

    public void setCantidadTotalProductos(Integer cantidadTotalProductos) {
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<LineaComprobante> getLineas() {
        return lineas;
    }

    public void addLinea(LineaComprobante linea) {
        lineas.add(linea);
        linea.setComprobante(this);
    }

    public void removeLinea(LineaComprobante linea) {
        lineas.remove(linea);
        linea.setComprobante(null);
    }
}