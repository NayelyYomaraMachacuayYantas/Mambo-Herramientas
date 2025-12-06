package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.tipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 📌 Este repository hereda de JpaRepository
//    - No hace falta programar nada extra.
//    - Nos da CRUD listo: save(), findAll(), findById(), deleteById(), etc.
@Repository
public interface TipoDocumentoRepository extends JpaRepository<tipoDocumento, Long> {
}
