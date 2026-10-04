package com.ejemplo.ventas.service;

import com.ejemplo.ventas.dto.ComprobanteRequest;
import com.ejemplo.ventas.dto.ComprobanteResponse;
import com.ejemplo.ventas.dto.LineaComprobanteRequest;
import com.ejemplo.ventas.entity.Cliente;
import com.ejemplo.ventas.entity.Comprobante;
import com.ejemplo.ventas.entity.LineaComprobante;
import com.ejemplo.ventas.entity.Producto;
import com.ejemplo.ventas.exception.ComprobanteNotFoundException;
import com.ejemplo.ventas.exception.RecursoNoEncontradoException;
import com.ejemplo.ventas.repository.ClienteRepository;
import com.ejemplo.ventas.repository.ComprobanteRepository;
import com.ejemplo.ventas.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public ComprobanteService(
            ComprobanteRepository comprobanteRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public ComprobanteResponse crear(ComprobanteRequest request) {
        Cliente cliente = clienteRepository.findById(request.getCliente().getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con ID " + request.getCliente().getClienteId()));

        BigDecimal total = BigDecimal.ZERO;
        int cantidadTotal = 0;
        Comprobante comprobante = new Comprobante(
                LocalDateTime.now(), BigDecimal.ZERO, 0, cliente);

        for (LineaComprobanteRequest lineaRequest : request.getLineas()) {
            Producto producto = productoRepository.findById(lineaRequest.getProducto().getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un producto con ID " + lineaRequest.getProducto().getProductoId()));

            BigDecimal precioHistorico = producto.getPrecio();
            BigDecimal subtotal = precioHistorico.multiply(BigDecimal.valueOf(lineaRequest.getCantidad()));
            LineaComprobante linea = new LineaComprobante(
                    lineaRequest.getCantidad(), precioHistorico, subtotal, producto);
            comprobante.addLinea(linea);

            total = total.add(subtotal);
            cantidadTotal += lineaRequest.getCantidad();
        }

        comprobante.setTotal(total);
        comprobante.setCantidadTotalProductos(cantidadTotal);
        return ComprobanteResponse.fromEntity(comprobanteRepository.save(comprobante));
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse obtenerPorId(Long id) {
        Comprobante comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new ComprobanteNotFoundException(id));
        return ComprobanteResponse.fromEntity(comprobante);
    }

    @Transactional(readOnly = true)
    public List<ComprobanteResponse> listar() {
        return comprobanteRepository.findAll().stream()
                .map(ComprobanteResponse::fromEntity)
                .toList();
    }
}