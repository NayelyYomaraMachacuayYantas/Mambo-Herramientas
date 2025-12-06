package com.sistemaFacturacion.Mambo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private cliente cliente;

    // 🔹 Relación 1:1 con Carrito
    @OneToOne
    @JoinColumn(name = "carrito_id", nullable = false, unique = true)
    private carrito carrito;

    // 🔹 Tipo de comprobante: BOLETA o FACTURA
    @Column(nullable = false, length = 20)
    private String tipo;

    // 🔹 Copias fijas de datos del cliente (para mantener histórico)
    @Column(nullable = false, length = 100)
    private String nombreCliente;

    @Column(nullable = false, length = 20)
    private String tipoDocumento;

    @Column(nullable = false, length = 20)
    private String numeroDocumento;

    // 🔹 Fecha de emisión
    private LocalDateTime fechaCreacion = LocalDateTime.now();


    // 🔹 Total pagado
    @Column(nullable = false)
    private Double total;
}
