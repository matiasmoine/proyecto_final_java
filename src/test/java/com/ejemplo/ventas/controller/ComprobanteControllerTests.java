package com.ejemplo.ventas.controller;

import com.ejemplo.ventas.entity.Cliente;
import com.ejemplo.ventas.entity.Producto;
import com.ejemplo.ventas.repository.ClienteRepository;
import com.ejemplo.ventas.repository.ComprobanteRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ComprobanteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @BeforeEach
    void limpiarBase() {
        comprobanteRepository.deleteAll();
        productoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void creaComprobanteCalculaTotalYGuardaPrecioHistorico() throws Exception {
        Cliente cliente = clienteRepository.save(
                new Cliente("Juan", "juan@example.com", "123"));
        Producto producto = productoRepository.save(
                new Producto("Producto", new BigDecimal("100.00"), 10));

        mockMvc.perform(post("/comprobantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": { "clienteid": %d },
                                  "lineas": [
                                    {
                                      "cantidad": 2,
                                      "producto": { "productoid": %d }
                                    }
                                  ]
                                }
                                """.formatted(cliente.getClienteId(), producto.getProductoId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comprobanteId").isNumber())
                .andExpect(jsonPath("$.total", is(200.0)))
                .andExpect(jsonPath("$.cantidadTotalProductos", is(2)))
                .andExpect(jsonPath("$.lineas", hasSize(1)))
                .andExpect(jsonPath("$.lineas[0].precioUnitario", is(100.0)))
                .andExpect(jsonPath("$.lineas[0].subtotal", is(200.0)));

        Producto actualizado = productoRepository.findById(producto.getProductoId()).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(actualizado.getStock()).isEqualTo(8);
    }

    @Test
    void devuelve404SiClienteNoExiste() throws Exception {
        Producto producto = productoRepository.save(
                new Producto("Producto", new BigDecimal("100.00"), 10));

        mockMvc.perform(post("/comprobantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": { "clienteId": 9999 },
                                  "lineas": [
                                    {
                                      "cantidad": 1,
                                      "producto": { "productoId": %d }
                                    }
                                  ]
                                }
                                """.formatted(producto.getProductoId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void rechazaCantidadCero() throws Exception {
        Cliente cliente = clienteRepository.save(
                new Cliente("Juan", "juan@example.com", "123"));
        Producto producto = productoRepository.save(
                new Producto("Producto", new BigDecimal("100.00"), 10));

        mockMvc.perform(post("/comprobantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": { "clienteId": %d },
                                  "lineas": [
                                    {
                                      "cantidad": 0,
                                      "producto": { "productoId": %d }
                                    }
                                  ]
                                }
                                """.formatted(cliente.getClienteId(), producto.getProductoId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultaComprobanteInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/comprobantes/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rechazaStockInsuficienteSinCrearComprobanteNiModificarStock() throws Exception {
        Cliente cliente = clienteRepository.save(
                new Cliente("Juan", "juan@example.com", "123"));
        Producto producto = productoRepository.save(
                new Producto("Producto", new BigDecimal("100.00"), 10));

        mockMvc.perform(post("/comprobantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": { "clienteId": %d },
                                  "lineas": [
                                    {
                                      "cantidad": 11,
                                      "producto": { "productoId": %d }
                                    }
                                  ]
                                }
                                """.formatted(cliente.getClienteId(), producto.getProductoId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)));

        org.assertj.core.api.Assertions.assertThat(productoRepository.findById(producto.getProductoId())
                        .orElseThrow().getStock())
                .isEqualTo(10);
        org.assertj.core.api.Assertions.assertThat(comprobanteRepository.count()).isZero();
    }

    @Test
    void validaTodasLasLineasAntesDeDescontarStock() throws Exception {
        Cliente cliente = clienteRepository.save(
                new Cliente("Juan", "juan@example.com", "123"));
        Producto productoDisponible = productoRepository.save(
                new Producto("Disponible", new BigDecimal("100.00"), 10));
        Producto productoAgotado = productoRepository.save(
                new Producto("Agotado", new BigDecimal("50.00"), 1));

        mockMvc.perform(post("/comprobantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": { "clienteId": %d },
                                  "lineas": [
                                    {
                                      "cantidad": 2,
                                      "producto": { "productoId": %d }
                                    },
                                    {
                                      "cantidad": 2,
                                      "producto": { "productoId": %d }
                                    }
                                  ]
                                }
                                """.formatted(cliente.getClienteId(), productoDisponible.getProductoId(), productoAgotado.getProductoId())))
                .andExpect(status().isConflict());

        org.assertj.core.api.Assertions.assertThat(productoRepository.findById(productoDisponible.getProductoId())
                        .orElseThrow().getStock())
                .isEqualTo(10);
        org.assertj.core.api.Assertions.assertThat(comprobanteRepository.count()).isZero();
    }
}