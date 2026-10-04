package com.ejemplo.ventas.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WorldClockResponse(
        @JsonProperty("currentDateTime") String currentDateTime,
        @JsonProperty("utcOffset") String utcOffset) {
}