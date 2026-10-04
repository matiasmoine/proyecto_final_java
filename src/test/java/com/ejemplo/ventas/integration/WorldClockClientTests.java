package com.ejemplo.ventas.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WorldClockClientTests {

    @Test
    void usaLaFechaDevueltaPorWorldClock() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        WorldClockClient client = WorldClockClient.forTesting(builder);

        server.expect(requestTo(""))
                .andRespond(withSuccess("""
                        {
                          "currentDateTime": "2026-10-04T15:30:00+00:00",
                          "utcOffset": "00:00:00"
                        }
                        """, org.springframework.http.MediaType.APPLICATION_JSON));

        assertThat(client.obtenerFecha()).isEqualTo(LocalDateTime.of(2026, 10, 4, 15, 30));
        server.verify();
    }

    @Test
    void usaFechaLocalCuandoWorldClockFalla() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        WorldClockClient client = WorldClockClient.forTesting(builder);

        server.expect(requestTo(""))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        LocalDateTime fecha = client.obtenerFecha();
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        assertThat(fecha).isBetween(antes, despues);
        server.verify();
    }
}