package com.ejemplo.ventas.repository;

import com.ejemplo.ventas.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}