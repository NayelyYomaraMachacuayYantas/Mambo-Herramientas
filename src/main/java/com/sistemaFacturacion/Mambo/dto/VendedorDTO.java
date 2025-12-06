package com.sistemaFacturacion.Mambo.dto;

import lombok.Data;

@Data
public class VendedorDTO {
    private Long id;
    private String nombreCompleto;
    private String numeroDocumento;
    private String email;
    private String telefono;
    private String contra;
}