package com.ejemplo.ventas.controller;

import com.ejemplo.ventas.entity.Producto;
import com.ejemplo.ventas.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void limpiarBase() {
        productoRepository.deleteAll();
    }

    @Test
    void creaProductoYDevuelve201() throws Exception {
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Producto de prueba",
                                  "precio": 100.00,
                                  "stock": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productoId").isNumber())
                .andExpect(jsonPath("$.descripcion", is("Producto de prueba")))
                .andExpect(jsonPath("$.stock", is(10)));
    }

    @Test
    void obtieneYListaProductos() throws Exception {
        Producto producto = productoRepository.save(
                new Producto("Producto A", new BigDecimal("25.50"), 4));

        mockMvc.perform(get("/productos/{id}", producto.getProductoId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio", is(25.5)))
                .andExpect(jsonPath("$.stock", is(4)));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void actualizaProducto() throws Exception {
        Producto producto = productoRepository.save(
                new Producto("Producto A", new BigDecimal("25.50"), 4));

        mockMvc.perform(put("/productos/{id}", producto.getProductoId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Producto actualizado",
                                  "precio": 150.00,
                                  "stock": 20
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion", is("Producto actualizado")))
                .andExpect(jsonPath("$.precio", is(150.0)))
                .andExpect(jsonPath("$.stock", is(20)));
    }

    @Test
    void rechazaPrecioNegativo() throws Exception {
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Producto inválido",
                                  "precio": -1.00,
                                  "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.precio", is("El precio no puede ser negativo")));
    }

    @Test
    void rechazaStockNegativo() throws Exception {
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Producto inválido",
                                  "precio": 10.00,
                                  "stock": -1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.stock", is("El stock no puede ser negativo")));
    }

    @Test
    void devuelve404SiProductoNoExiste() throws Exception {
        mockMvc.perform(get("/productos/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }
}