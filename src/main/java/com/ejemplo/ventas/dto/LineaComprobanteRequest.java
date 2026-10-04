package com.ejemplo.ventas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LineaComprobanteRequest {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que cero")
    private Integer cantidad;

    @NotNull(message = "El producto es obligatorio")
    @Valid
    private ProductoIdRequest producto;

    public LineaComprobanteRequest() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoIdRequest getProducto() {
        return producto;
    }

    public void setProducto(ProductoIdRequest producto) {
        this.producto = producto;
    }
}