package com.ejemplo.ventas.repository;

import com.ejemplo.ventas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}