package com.ejemplo.ventas.exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long id) {
        super("No existe un producto con ID " + id);
    }
}