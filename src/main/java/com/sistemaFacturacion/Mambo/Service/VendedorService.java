package com.sistemaFacturacion.Mambo.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sistemaFacturacion.Mambo.Repository.UsuarioRepository;
import com.sistemaFacturacion.Mambo.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.Repository.TipoDocumentoRepository;
import com.sistemaFacturacion.Mambo.dto.VendedorDTO;
import com.sistemaFacturacion.Mambo.model.Usuario;
import com.sistemaFacturacion.Mambo.model.rol;

@Service
public class VendedorService {

    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public VendedorService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                           PasswordEncoder passwordEncoder, TipoDocumentoRepository tipoDocumentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }
    
    public List<Usuario> obtenerVendedores() {
        return usuarioRepository.listarVendedores();
    }

    public List<VendedorDTO> listar() {
        return usuarioRepository.findByRolNombre("VENDEDOR").stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional
    public VendedorDTO guardar(VendedorDTO dto) {
        Usuario u;
        boolean esEdicion = false;

        if (dto.getId() != null) {
            u = usuarioRepository.findById(dto.getId()).orElseThrow(() ->
                    new RuntimeException("Vendedor no encontrado"));
            esEdicion = true;
        } else {
            u = new Usuario();
        }

        u.setNombreCompleto(dto.getNombreCompleto());
        u.setNumeroDocumento(dto.getNumeroDocumento());
        u.setEmail(dto.getEmail());
        u.setTelefono(dto.getTelefono());

        if (dto.getContra() == null || dto.getContra().isEmpty()) {
            if (!esEdicion) {
                u.setContra(passwordEncoder.encode("123456"));
            }
        } else {
            u.setContra(passwordEncoder.encode(dto.getContra()));
        }

        rol rolVendedor = rolRepository.findByNombre("VENDEDOR")
                .orElseThrow(() -> new RuntimeException("Rol VENDEDOR no existe"));
        u.setRol(rolVendedor);

        Usuario guardado = usuarioRepository.save(u);

        return convertirADTO(guardado);
    }

    public VendedorDTO buscarDTO(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    private VendedorDTO convertirADTO(Usuario u) {
        VendedorDTO dto = new VendedorDTO();
        dto.setId(u.getId());
        dto.setNombreCompleto(u.getNombreCompleto());
        dto.setNumeroDocumento(u.getNumeroDocumento());
        dto.setEmail(u.getEmail());
        dto.setTelefono(u.getTelefono());
        return dto;
    }
}