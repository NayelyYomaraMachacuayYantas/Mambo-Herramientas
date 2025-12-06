package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    Optional<Producto> findById(Long id);
}
