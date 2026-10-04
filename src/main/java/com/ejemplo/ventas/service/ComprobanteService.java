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
import com.ejemplo.ventas.exception.StockInsuficienteException;
import com.ejemplo.ventas.repository.ClienteRepository;
import com.ejemplo.ventas.repository.ComprobanteRepository;
import com.ejemplo.ventas.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        Map<Long, Producto> productos = new LinkedHashMap<>();
        Map<Long, Integer> cantidadesPorProducto = new LinkedHashMap<>();

        // Primero se buscan todos los productos y se valida el stock acumulado.
        // Todavía no se modifica ninguna entidad en esta etapa.
        for (LineaComprobanteRequest lineaRequest : request.getLineas()) {
            Long productoId = lineaRequest.getProducto().getProductoId();
            Producto producto = productos.computeIfAbsent(productoId, id -> productoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un producto con ID " + id)));
            cantidadesPorProducto.merge(productoId, lineaRequest.getCantidad(), Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : cantidadesPorProducto.entrySet()) {
            Producto producto = productos.get(entry.getKey());
            Integer cantidadSolicitada = entry.getValue();
            if (producto.getStock() < cantidadSolicitada) {
                throw new StockInsuficienteException(
                        producto.getDescripcion(), producto.getStock(), cantidadSolicitada);
            }
        }

        for (LineaComprobanteRequest lineaRequest : request.getLineas()) {
            Producto producto = productos.get(lineaRequest.getProducto().getProductoId());

            BigDecimal precioHistorico = producto.getPrecio();
            BigDecimal subtotal = precioHistorico.multiply(BigDecimal.valueOf(lineaRequest.getCantidad()));
            LineaComprobante linea = new LineaComprobante(
                    lineaRequest.getCantidad(), precioHistorico, subtotal, producto);
            comprobante.addLinea(linea);

            total = total.add(subtotal);
            cantidadTotal += lineaRequest.getCantidad();
        }

        for (Map.Entry<Long, Integer> entry : cantidadesPorProducto.entrySet()) {
            Producto producto = productos.get(entry.getKey());
            producto.setStock(producto.getStock() - entry.getValue());
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