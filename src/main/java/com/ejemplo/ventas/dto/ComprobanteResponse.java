package com.ejemplo.ventas.dto;

import com.ejemplo.ventas.entity.Comprobante;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ComprobanteResponse(
        Long comprobanteId,
        LocalDateTime fecha,
        BigDecimal total,
        Integer cantidadTotalProductos,
        Long clienteId,
        List<LineaComprobanteResponse> lineas) {

    public static ComprobanteResponse fromEntity(Comprobante comprobante) {
        return new ComprobanteResponse(
                comprobante.getComprobanteId(),
                comprobante.getFecha(),
                comprobante.getTotal(),
                comprobante.getCantidadTotalProductos(),
                comprobante.getCliente().getClienteId(),
                comprobante.getLineas().stream()
                        .map(LineaComprobanteResponse::fromEntity)
                        .toList()
        );
    }
}