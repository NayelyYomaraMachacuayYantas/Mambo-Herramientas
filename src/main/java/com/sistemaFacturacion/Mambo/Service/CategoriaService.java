package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.Repository.CategoriaRepository;
import com.sistemaFacturacion.Mambo.model.categoria;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // ➕ Crear o actualizar categoría
    public categoria guardarCategoria(categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // 🔎 Buscar categoría por id
    public Optional<categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // 📋 Listar todas las categorías
    public List<categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // ❌ Eliminar categoría
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    // 🔎 Buscar categoría por nombre
    public Optional<categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
}
