package com.sistemaFacturacion.Mambo.controller.RestController;

import com.sistemaFacturacion.Mambo.Service.ClienteService;
import com.sistemaFacturacion.Mambo.dto.ClienteDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
    
    private final ClienteService clienteService;

    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Crear cliente


    // Listar todos los clientes
    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteService.listarClientes();
    }

    // Obtener cliente por ID
    @GetMapping("/buscar/{id}")
    public Optional<ClienteDTO> obtener(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    // Buscar cliente por email
    @GetMapping("/buscar/email/{email}")
    public Optional<ClienteDTO> buscarPorEmail(@PathVariable String email) {
        return clienteService.buscarPorEmail(email);
    }



    // Actualizar cliente
    @PutMapping("/actualizar/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @RequestBody ClienteDTO c) {
        return clienteService.actualizarCliente(id, c);
    }

    // Eliminar cliente
    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "Cliente eliminado con ID: " + id;
    }
}

