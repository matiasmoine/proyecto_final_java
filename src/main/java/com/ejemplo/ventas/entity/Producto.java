package com.ejemplo.ventas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;

    private String descripcion;
    private BigDecimal precio;
    private Integer stock;

    @OneToMany(mappedBy = "producto")
    private List<LineaComprobante> lineas = new ArrayList<>();

    protected Producto() {
    }

    public Producto(String descripcion, BigDecimal precio, Integer stock) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<LineaComprobante> getLineas() {
        return lineas;
    }
}