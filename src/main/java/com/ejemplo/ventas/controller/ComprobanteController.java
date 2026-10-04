package com.ejemplo.ventas.controller;

import com.ejemplo.ventas.dto.ComprobanteRequest;
import com.ejemplo.ventas.dto.ComprobanteResponse;
import com.ejemplo.ventas.service.ComprobanteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @PostMapping
    public ResponseEntity<ComprobanteResponse> crear(@Valid @RequestBody ComprobanteRequest request) {
        ComprobanteResponse response = comprobanteService.crear(request);
        return ResponseEntity.created(URI.create("/comprobantes/" + response.comprobanteId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comprobanteService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ComprobanteResponse>> listar() {
        return ResponseEntity.ok(comprobanteService.listar());
    }
}