package com.ejemplo.ventas.repository;

import com.ejemplo.ventas.entity.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
}