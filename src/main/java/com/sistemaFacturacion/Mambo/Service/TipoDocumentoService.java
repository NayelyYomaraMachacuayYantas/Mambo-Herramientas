
package com.sistemaFacturacion.Mambo.Service;


import com.sistemaFacturacion.Mambo.Repository.TipoDocumentoRepository;
import com.sistemaFacturacion.Mambo.model.tipoDocumento;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    // 📌 Inyección de dependencia vía constructor
    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    // 📌 Listar todos los tipos de documentos
    public List<tipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }

    // 📌 Buscar un tipo de documento por su ID
    public Optional<tipoDocumento> buscarPorId(Long id) {
        return tipoDocumentoRepository.findById(id);
    }

    // 📌 Guardar o actualizar un tipo de documento
    public tipoDocumento guardar(tipoDocumento td) {
        return tipoDocumentoRepository.save(td);
    }

    // 📌 Eliminar un tipo de documento por ID
    public void eliminar(Long id) {
        tipoDocumentoRepository.deleteById(id);
    }
}
