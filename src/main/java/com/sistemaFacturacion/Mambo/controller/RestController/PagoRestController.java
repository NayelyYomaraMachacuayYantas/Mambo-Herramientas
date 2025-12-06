package com.sistemaFacturacion.Mambo.controller.RestController;

import com.sistemaFacturacion.Mambo.dto.PagoRequestDTO;
import com.sistemaFacturacion.Mambo.dto.PagoResponseDTO;
import com.sistemaFacturacion.Mambo.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoRestController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/procesar")
    public ResponseEntity<PagoResponseDTO> procesarPago(@RequestBody PagoRequestDTO request) {
        PagoResponseDTO response = pagoService.procesarPago(request);
        return ResponseEntity.ok(response);
    }
}