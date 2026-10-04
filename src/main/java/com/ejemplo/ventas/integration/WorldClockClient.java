package com.ejemplo.ventas.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class WorldClockClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldClockClient.class);

    private final RestClient restClient;

    public WorldClockClient(
            RestClient.Builder restClientBuilder,
            @Value("${world-clock.url}") String url) {
        this.restClient = restClientBuilder.baseUrl(url).build();
    }

    public static WorldClockClient forTesting(RestClient.Builder restClientBuilder) {
        return new WorldClockClient(restClientBuilder, "");
    }

    public LocalDateTime obtenerFecha() {
        try {
            WorldClockResponse response = restClient.get()
                    .retrieve()
                    .body(WorldClockResponse.class);

            if (response == null || response.currentDateTime() == null
                    || response.currentDateTime().isBlank()) {
                throw new IllegalStateException("La respuesta de World Clock no contiene fecha");
            }

            return parsearFecha(response.currentDateTime());
        } catch (Exception exception) {
            LocalDateTime fallback = LocalDateTime.now();
            LOGGER.warn("No se pudo obtener la fecha de World Clock; se usa fecha local {}. Motivo: {}",
                    fallback, exception.getMessage());
            return fallback;
        }
    }

    private LocalDateTime parsearFecha(String value) {
        try {
            return OffsetDateTime.parse(value).toLocalDateTime();
        } catch (Exception ignored) {
            return LocalDateTime.parse(value);
        }
    }
}