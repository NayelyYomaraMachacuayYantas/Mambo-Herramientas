package com.sistemaFacturacion.Mambo.controller.RestController;


import com.sistemaFacturacion.Mambo.Service.ClienteService;
import com.sistemaFacturacion.Mambo.Service.DetalleCarritoService;
import com.sistemaFacturacion.Mambo.dto.CarritoDTO;
import com.sistemaFacturacion.Mambo.model.cliente;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final DetalleCarritoService detalleCarritoService;
    private final ClienteService clienteService;

    public CarritoController(DetalleCarritoService detalleCarritoService, ClienteService clienteService) {
        this.detalleCarritoService = detalleCarritoService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<CarritoDTO> guardarCarrito(@RequestBody CarritoDTO carritoDTO, Principal principal) {
        
        String numDocumento = principal.getName();
        // Buscar cliente
        cliente c = clienteService.buscarPorNumeroDocumento(numDocumento);

        // Guardar carrito + detalles
        CarritoDTO resultado = detalleCarritoService.guardarCarrito(carritoDTO, c);

        return ResponseEntity.ok(resultado);
    }
}
