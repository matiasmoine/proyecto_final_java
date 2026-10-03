package com.ejemplo.ventas.service;

import com.ejemplo.ventas.dto.ClienteRequest;
import com.ejemplo.ventas.dto.ClienteResponse;
import com.ejemplo.ventas.entity.Cliente;
import com.ejemplo.ventas.exception.ClienteNotFoundException;
import com.ejemplo.ventas.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = new Cliente(request.getNombre(), request.getEmail(), request.getDocumento());
        return ClienteResponse.fromEntity(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Long id) {
        return ClienteResponse.fromEntity(buscarCliente(id));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponse::fromEntity)
                .toList();
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarCliente(id);
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setDocumento(request.getDocumento());
        return ClienteResponse.fromEntity(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        Cliente cliente = buscarCliente(id);
        clienteRepository.delete(cliente);
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }
}