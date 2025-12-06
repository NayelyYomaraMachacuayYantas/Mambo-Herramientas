package com.sistemaFacturacion.Mambo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "detalleCarrito")
public class detalleCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private carrito carrito;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    Producto producto;

    @Column(nullable = false)
    private int cantidad;

    public Double subTotal;

}
