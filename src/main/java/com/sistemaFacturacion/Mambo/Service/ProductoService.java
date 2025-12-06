package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.model.Producto;
import com.sistemaFacturacion.Mambo.model.categoria;
import com.sistemaFacturacion.Mambo.Repository.CategoriaRepository;
import com.sistemaFacturacion.Mambo.Repository.ProductoRepository;
import com.sistemaFacturacion.Mambo.dto.ProductoDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Crear producto
    public Producto save(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        categoria categoria = categoriaRepository.findById(dto.getCategoriaID())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        // Guardar imagen
        if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
            producto.setImagenUrl(guardarImagen(dto.getImagen()));
        }

        return productoRepository.save(producto);
    }

    // Actualizar producto
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        categoria categoria = categoriaRepository.findById(dto.getCategoriaID())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        // Si sube nueva imagen, reemplaza la anterior
        if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
            producto.setImagenUrl(guardarImagen(dto.getImagen()));
        }

        return productoRepository.save(producto);
    }

    // Guardar imagen en carpeta static/img/
    private String guardarImagen(MultipartFile file) {
        try {
            String uploadDir = new File("src/main/resources/static/img/").getAbsolutePath();
            File directorio = new File(uploadDir);
            if (!directorio.exists()) directorio.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destino = new File(directorio, fileName);
            file.transferTo(destino);

            return "/img/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }


    public ProductoDTO findDtoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoriaID(producto.getCategoria().getId());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.calcularEstadoStock();
        return dto;
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto saveDesdeJson(ProductoDTO dto) {
    Producto producto = new Producto();
    producto.setNombre(dto.getNombre());
    producto.setDescripcion(dto.getDescripcion());
    producto.setPrecio(dto.getPrecio());
    producto.setStock(dto.getStock());

    categoria categoria = categoriaRepository.findById(dto.getCategoriaID())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    producto.setCategoria(categoria);

    producto.setImagenUrl(dto.getImagenUrl()); // Directamente desde JSON

    return productoRepository.save(producto);
}

public Producto actualizarDesdeJson(Long id, ProductoDTO dto) {
    Producto producto = productoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    
    producto.setNombre(dto.getNombre());
    producto.setDescripcion(dto.getDescripcion());
    producto.setPrecio(dto.getPrecio());
    producto.setStock(dto.getStock());

    categoria categoria = categoriaRepository.findById(dto.getCategoriaID())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    producto.setCategoria(categoria);

    producto.setImagenUrl(dto.getImagenUrl());

    return productoRepository.save(producto);
}

public Optional<Producto> findById(Long id) {
    return productoRepository.findById(id);
}
}
