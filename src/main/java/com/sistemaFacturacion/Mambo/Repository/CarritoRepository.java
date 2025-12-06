package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<carrito, Long> {
    // Buscar todos los carritos de un cliente
    List<carrito> findByClienteId(Long clienteId);

    // Buscar carrito por comprobante
    carrito findByComprobanteId(Long comprobanteId);
}
