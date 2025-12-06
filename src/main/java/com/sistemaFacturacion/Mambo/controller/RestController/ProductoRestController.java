package com.sistemaFacturacion.Mambo.controller.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemaFacturacion.Mambo.model.Producto;
import com.sistemaFacturacion.Mambo.Service.ProductoService;
import com.sistemaFacturacion.Mambo.dto.ProductoDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private final ProductoService productoService;

    public ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Listar todos
    // ✅ Listar todos los productos (entidades completas, útil si quieres imágenes y categorías)
    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    // ✅ Buscar por ID (devuelve DTO con categoríaID + urls de imágenes)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable Long id) {
        try {
            ProductoDTO dto = productoService.findDtoById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Crear nuevo producto desde API
    @PostMapping
public ResponseEntity<Producto> create(@RequestBody ProductoDTO productoDTO) {
    Producto producto = productoService.saveDesdeJson(productoDTO);
    return ResponseEntity.ok(producto);
}

    // ✅ Actualizar producto
    @PutMapping("/{id}")
public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
    Producto productoActualizado = productoService.actualizarDesdeJson(id, productoDTO);
    return ResponseEntity.ok(productoActualizado);
}

    // ✅ Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
