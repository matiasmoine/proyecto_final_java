package com.ejemplo.ventas.dto;

import com.ejemplo.ventas.entity.Producto;

import java.math.BigDecimal;

public record ProductoResponse(Long productoId, String descripcion, BigDecimal precio, Integer stock) {

    public static ProductoResponse fromEntity(Producto producto) {
        return new ProductoResponse(
                producto.getProductoId(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}