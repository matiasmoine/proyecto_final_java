package com.ejemplo.ventas.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("No existe un cliente con ID " + id);
    }
}