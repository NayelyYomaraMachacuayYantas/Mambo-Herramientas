package com.sistemaFacturacion.Mambo.Service;
import com.sistemaFacturacion.Mambo.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.model.rol;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Crear nuevo rol
    public rol crearRol(rol rol) {
        return rolRepository.save(rol);
    }

    // Listar todos los roles
    public List<rol> listarRoles() {
        return rolRepository.findAll();
    }

    // Buscar rol por ID
    public Optional<rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    // Buscar rol por nombre
    public Optional<rol> obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    // Actualizar rol
    public rol actualizarRol(Long id, rol rolActualizado) {
        return rolRepository.findById(id)
                .map(r -> {
                    r.setNombre(rolActualizado.getNombre());
                    return rolRepository.save(r);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    // Eliminar rol
    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}
