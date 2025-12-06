package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<categoria, Long> {
    // 🔎 Buscar categoría por nombre
    Optional<categoria> findByNombre(String nombre);
    Optional<categoria> findById(Long id);
}
