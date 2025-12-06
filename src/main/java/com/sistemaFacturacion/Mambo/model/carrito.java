package com.sistemaFacturacion.Mambo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data

public class carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id", referencedColumnName = "id")
    private pago pago;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private cliente cliente; // <-- cámbialo de cliente_id a cliente

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<detalleCarrito> detalles = new ArrayList<>();

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToOne(mappedBy = "carrito")
    private Comprobante comprobante;

    private Double total;

}
