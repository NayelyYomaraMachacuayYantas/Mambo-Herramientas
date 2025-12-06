package com.sistemaFacturacion.Mambo.controller.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemaFacturacion.Mambo.Service.TipoDocumentoService;
import com.sistemaFacturacion.Mambo.model.tipoDocumento;


@RestController // 📌 Este controller devuelve JSON (API REST)
@RequestMapping("/api/tipo-documentos") // 📌 Ruta base
public class TipoDocRestController {
        private final TipoDocumentoService tipoDocumentoService;

    // 📌 Inyección del Service en el constructor
    public TipoDocRestController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    // 📌 GET → Listar todos
    @GetMapping
    public List<tipoDocumento> listar() {
        return tipoDocumentoService.listarTodos();
    }

    // 📌 GET → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<tipoDocumento> obtenerPorId(@PathVariable Long id) {
        return tipoDocumentoService.buscarPorId(id)
                .map(ResponseEntity::ok) // Si lo encuentra → 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no existe → 404 Not Found
    }

    // 📌 POST → Crear nuevo tipo de documento
    @PostMapping
    public tipoDocumento crear(@RequestBody tipoDocumento td) {
        return tipoDocumentoService.guardar(td);
    }

    // 📌 PUT → Actualizar tipo de documento existente
    @PutMapping("/{id}")
    public ResponseEntity<tipoDocumento> actualizar(@PathVariable Long id, @RequestBody tipoDocumento td) {
        return tipoDocumentoService.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(td.getNombre()); // Cambiamos solo el nombre
                    return ResponseEntity.ok(tipoDocumentoService.guardar(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 📌 DELETE → Eliminar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (tipoDocumentoService.buscarPorId(id).isPresent()) {
            tipoDocumentoService.eliminar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
