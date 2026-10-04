package com.ejemplo.ventas.aspect;

import com.ejemplo.ventas.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogAspectTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void limpiarBase() {
        clienteRepository.deleteAll();
    }

    @Test
    void aspectoEstaRegistradoYInterceptaControllers() throws Exception {
        assertThat(applicationContext.getBean(LogAspect.class)).isNotNull();

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }
}