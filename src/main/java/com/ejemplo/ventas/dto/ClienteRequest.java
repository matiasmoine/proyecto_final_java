package com.ejemplo.ventas.dto;

import jakarta.validation.constraints.NotBlank;

public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String email;
    private String documento;

    public ClienteRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}