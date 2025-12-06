package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<cliente, Long> {

    // Listar todos los clientes activos
    // Buscar cliente por número de documento
    Optional<cliente> findByNumeroDocumento(String numeroDocumento);

    Optional<cliente> findByEmail(String email);
}
