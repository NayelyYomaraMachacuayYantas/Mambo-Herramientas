package com.sistemaFacturacion.Mambo.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private categoria categoria;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String descripcion; // ✅ agregado
    
    private String imagenUrl; // ✅ Una sola imagen

}
