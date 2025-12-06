package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.dto.PagoRequestDTO;
import com.sistemaFacturacion.Mambo.dto.PagoResponseDTO;
import com.sistemaFacturacion.Mambo.model.pago;
import com.sistemaFacturacion.Mambo.Repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public PagoResponseDTO procesarPago(PagoRequestDTO request) {
        pago pago = new pago();
        pago.setPrecio(request.getPrecio());
        pago.setMetodo(request.getMetodo());
        pago.setFechaPago(LocalDateTime.now());

        // Simulación de validación
        if (request.getPrecio() != null && request.getPrecio() > 0) {
            pago.setEstado("APROBADO");
        } else {
            pago.setEstado("RECHAZADO");
        }

        pagoRepository.save(pago);

        PagoResponseDTO response = new PagoResponseDTO();
        response.setMensaje("Pago procesado correctamente");
        response.setEstado(pago.getEstado());
        response.setFechaPago(pago.getFechaPago());
        return response;
    }
}