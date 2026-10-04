package com.ejemplo.ventas.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public class ClienteIdRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    @JsonAlias({"clienteid", "clienteId"})
    private Long clienteId;

    public ClienteIdRequest() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}