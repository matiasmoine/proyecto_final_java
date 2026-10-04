package com.ejemplo.ventas.dto;

import com.ejemplo.ventas.entity.LineaComprobante;

import java.math.BigDecimal;

public record LineaComprobanteResponse(
        Long lineaId,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        Long productoId,
        String descripcionProducto) {

    public static LineaComprobanteResponse fromEntity(LineaComprobante linea) {
        return new LineaComprobanteResponse(
                linea.getLineaId(),
                linea.getCantidad(),
                linea.getPrecioUnitario(),
                linea.getSubtotal(),
                linea.getProducto().getProductoId(),
                linea.getProducto().getDescripcion()
        );
    }
}