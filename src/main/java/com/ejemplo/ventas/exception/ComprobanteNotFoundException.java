package com.ejemplo.ventas.exception;

public class ComprobanteNotFoundException extends RuntimeException {

    public ComprobanteNotFoundException(Long id) {
        super("No existe un comprobante con ID " + id);
    }
}