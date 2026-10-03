package com.ejemplo.ventas.dto;

import com.ejemplo.ventas.entity.Cliente;

public record ClienteResponse(Long clienteId, String nombre, String email, String documento) {

    public static ClienteResponse fromEntity(Cliente cliente) {
        return new ClienteResponse(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getDocumento()
        );
    }
}