package com.sistemaFacturacion.Mambo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {

    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private LocalDateTime fechaCreacion;
    private List<DetalleCarritoDto> detalles;
    private Double total;

    // 💳 Campos relacionados al pago (opcional)
    private Long pagoId;
    private String metodoPago;
    private String estadoPago;
    private Double precioPago;
}
