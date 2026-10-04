package com.ejemplo.ventas.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public class ProductoIdRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    @JsonAlias({"productoid", "productoId"})
    private Long productoId;

    public ProductoIdRequest() {
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}