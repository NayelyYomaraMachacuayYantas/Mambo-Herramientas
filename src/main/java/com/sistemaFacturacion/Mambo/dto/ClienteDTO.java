package com.sistemaFacturacion.Mambo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long id;
    private Long tipoDocumento;
    private String tipoDocumentoNombre;
    private String numeroDocumento;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String contra;
}
