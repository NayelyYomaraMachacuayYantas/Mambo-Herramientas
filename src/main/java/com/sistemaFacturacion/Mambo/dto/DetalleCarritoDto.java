package com.sistemaFacturacion.Mambo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCarritoDto {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Double precioUnitario;
    private int cantidad;
    private Double subtotal;
}
