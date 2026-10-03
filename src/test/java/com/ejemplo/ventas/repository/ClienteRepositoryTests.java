package com.ejemplo.ventas.repository;

import com.ejemplo.ventas.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void guardaYConsultaUnCliente() {
        Cliente cliente = new Cliente("Ana Pérez", "ana@example.com", "12345678");

        Cliente guardado = clienteRepository.save(cliente);

        assertThat(guardado.getClienteId()).isNotNull();
        assertThat(clienteRepository.findById(guardado.getClienteId()))
                .isPresent()
                .get()
                .extracting(Cliente::getNombre)
                .isEqualTo("Ana Pérez");
    }
}