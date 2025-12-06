package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.dto.PagoRequestDTO;
import com.sistemaFacturacion.Mambo.dto.PagoResponseDTO;

public interface PagoService {
    PagoResponseDTO procesarPago(PagoRequestDTO request);
}