package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<pago, Long> {
}
