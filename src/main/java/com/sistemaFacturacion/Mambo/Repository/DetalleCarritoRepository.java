package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.detalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<detalleCarrito, Long> {

    // Buscar todos los detalles de un carrito específico
    List<detalleCarrito> findByCarritoId(Long carritoId);

    // Buscar todos los detalles que correspondan a un producto específico
    List<detalleCarrito> findByProductoId(Long productoId);
}
