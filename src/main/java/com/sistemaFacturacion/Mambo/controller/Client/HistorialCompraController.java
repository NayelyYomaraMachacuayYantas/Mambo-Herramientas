package com.sistemaFacturacion.Mambo.controller.Client;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sistemaFacturacion.Mambo.Service.CarritoService;

@Controller
@RequestMapping("/Cliente")
public class HistorialCompraController {

    private final CarritoService historialCompraService;

    public HistorialCompraController(CarritoService historialCompraService) {
        this.historialCompraService = historialCompraService;
    }


}
