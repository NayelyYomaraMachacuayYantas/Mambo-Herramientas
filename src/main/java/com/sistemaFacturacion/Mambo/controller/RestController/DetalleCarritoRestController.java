package com.sistemaFacturacion.Mambo.controller.RestController;


import com.sistemaFacturacion.Mambo.Service.DetalleCarritoService;
import com.sistemaFacturacion.Mambo.dto.DetalleCarritoDto;
import com.sistemaFacturacion.Mambo.model.detalleCarrito;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles-carrito")
@CrossOrigin(origins = "*")
public class DetalleCarritoRestController {

    private final DetalleCarritoService detalleCarritoService;

    public DetalleCarritoRestController(DetalleCarritoService detalleCarritoService) {
        this.detalleCarritoService = detalleCarritoService;
    }

    // ➕ Crear un detalle
    @PostMapping
    public ResponseEntity<DetalleCarritoDto> crearDetalle(@RequestBody detalleCarrito detalle) {
        DetalleCarritoDto nuevoDetalle = detalleCarritoService.crearDetalle(detalle);
        return ResponseEntity.ok(nuevoDetalle);
    }

    // ✏️ Actualizar un detalle existente
    @PutMapping("/{id}")
    public ResponseEntity<DetalleCarritoDto> actualizarDetalle(@PathVariable Long id, @RequestBody detalleCarrito detalle) {
        DetalleCarritoDto actualizado = detalleCarritoService.actualizarDetalle(id, detalle);
        return ResponseEntity.ok(actualizado);
    }

    // ❌ Eliminar un detalle por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id) {
        detalleCarritoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }

    // 🔎 Obtener un detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCarritoDto> obtenerPorId(@PathVariable Long id) {
        Optional<DetalleCarritoDto> detalle = detalleCarritoService.obtenerPorId(id);
        return detalle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 📋 Listar todos los detalles
    @GetMapping
    public ResponseEntity<List<DetalleCarritoDto>> listarDetalles() {
        return ResponseEntity.ok(detalleCarritoService.listarDetalles());
    }

    // 🔍 Listar detalles por carrito
    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<List<DetalleCarritoDto>> listarPorCarrito(@PathVariable Long carritoId) {
        return ResponseEntity.ok(detalleCarritoService.listarPorCarrito(carritoId));
    }

    // 🔍 Listar detalles por producto
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<DetalleCarritoDto>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(detalleCarritoService.listarPorProducto(productoId));
    }
}
