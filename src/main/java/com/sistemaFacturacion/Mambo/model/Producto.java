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
    private categoria categoria; // corregido

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url") // <- esta es la clave
    private String imagenUrl; // usa nombres como "chaqueta_denim.jpg"

    public String getImagenPath() {
    if (imagenUrl == null || imagenUrl.isEmpty()) {
        return "/img/default.jpg"; // si no hay imagen
    }
    return "/img/" + imagenUrl;
}

}

