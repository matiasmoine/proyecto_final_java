package com.ejemplo.ventas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ComprobanteRequest {

    @NotNull(message = "El cliente es obligatorio")
    @Valid
    private ClienteIdRequest cliente;

    @NotEmpty(message = "El comprobante debe tener al menos una línea")
    @Valid
    private List<LineaComprobanteRequest> lineas;

    public ComprobanteRequest() {
    }

    public ClienteIdRequest getCliente() {
        return cliente;
    }

    public void setCliente(ClienteIdRequest cliente) {
        this.cliente = cliente;
    }

    public List<LineaComprobanteRequest> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaComprobanteRequest> lineas) {
        this.lineas = lineas;
    }
}