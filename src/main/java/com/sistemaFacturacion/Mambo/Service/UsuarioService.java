package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.Repository.UsuarioRepository;
import com.sistemaFacturacion.Mambo.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // 📌 Inyección de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 📌 Listar todos los usuarios
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 📌 Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // 📌 Guardar o actualizar usuario
    public Usuario guardar(Usuario usuario) {
        // Encriptar solo si es nueva o si la contraseña cambió
        if (usuario.getContra() != null) {
            usuario.setContra(passwordEncoder.encode(usuario.getContra()));
        }
        return usuarioRepository.save(usuario);
    }

    // 📌 Eliminar usuario por ID
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // 📌 Buscar por email (para login)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}
