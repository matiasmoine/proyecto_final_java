package com.ejemplo.ventas.service;

import com.ejemplo.ventas.dto.ProductoRequest;
import com.ejemplo.ventas.dto.ProductoResponse;
import com.ejemplo.ventas.entity.Producto;
import com.ejemplo.ventas.exception.ProductoNotFoundException;
import com.ejemplo.ventas.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto(request.getDescripcion(), request.getPrecio(), request.getStock());
        return ProductoResponse.fromEntity(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return ProductoResponse.fromEntity(buscarProducto(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream()
                .map(ProductoResponse::fromEntity)
                .toList();
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscarProducto(id);
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        return ProductoResponse.fromEntity(productoRepository.save(producto));
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
    }
}