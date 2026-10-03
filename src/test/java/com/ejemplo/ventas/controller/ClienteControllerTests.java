package com.ejemplo.ventas.controller;

import com.ejemplo.ventas.entity.Cliente;
import com.ejemplo.ventas.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void limpiarBase() {
        clienteRepository.deleteAll();
    }

    @Test
    void creaClienteYDevuelve201() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Juan Pérez",
                                  "email": "juan@example.com",
                                  "documento": "30111222"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").isNumber())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")));
    }

    @Test
    void listaClientes() throws Exception {
        clienteRepository.save(new Cliente("Ana", "ana@example.com", "123"));
        clienteRepository.save(new Cliente("Luis", "luis@example.com", "456"));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void actualizaCliente() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente("Ana", "ana@example.com", "123"));

        mockMvc.perform(put("/clientes/{id}", cliente.getClienteId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Ana Actualizada",
                                  "email": "ana.nueva@example.com",
                                  "documento": "999"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Ana Actualizada")))
                .andExpect(jsonPath("$.documento", is("999")));
    }

    @Test
    void devuelve404SiClienteNoExiste() throws Exception {
        mockMvc.perform(get("/clientes/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void devuelve400SiNombreEstaVacio() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "   ",
                                  "email": "invalido@example.com",
                                  "documento": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.nombre", is("El nombre es obligatorio")));
    }

    @Test
    void eliminaCliente() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente("Ana", "ana@example.com", "123"));

        mockMvc.perform(delete("/clientes/{id}", cliente.getClienteId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/clientes/{id}", cliente.getClienteId()))
                .andExpect(status().isNotFound());
    }
}