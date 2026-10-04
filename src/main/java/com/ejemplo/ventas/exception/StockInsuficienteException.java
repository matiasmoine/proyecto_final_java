package com.ejemplo.ventas.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String descripcion, Integer stockDisponible, Integer cantidadSolicitada) {
        super("Stock insuficiente para el producto '" + descripcion
                + "'. Disponible: " + stockDisponible
                + ", solicitado: " + cantidadSolicitada);
    }
}