package com.sistemaFacturacion.Mambo.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sistemaFacturacion.Mambo.Repository.ClienteRepository;
import com.sistemaFacturacion.Mambo.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.Repository.TipoDocumentoRepository;
import com.sistemaFacturacion.Mambo.dto.ClienteDTO;
import com.sistemaFacturacion.Mambo.model.cliente;
import com.sistemaFacturacion.Mambo.model.rol;
import com.sistemaFacturacion.Mambo.model.tipoDocumento;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TipoDocumentoRepository tDocumentoRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Constructor injection (recomendado)
    public ClienteService(
            ClienteRepository clienteRepository,
            TipoDocumentoRepository tDocumentoRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.clienteRepository = clienteRepository;
        this.tDocumentoRepository = tDocumentoRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Convertir DTO → Entidad
    private cliente convertirAEntidad(ClienteDTO dto) {
        cliente c = new cliente();
        c.setId(dto.getId());
        c.setNombreCompleto(dto.getNombreCompleto());
        c.setNumeroDocumento(dto.getNumeroDocumento());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());

        tipoDocumento tDocumento = tDocumentoRepository.findById(dto.getTipoDocumento())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
        c.setTipoDocumento(tDocumento);

        return c;
    }

    // ✅ Convertir Entidad → DTO
    private ClienteDTO convertirADTO(cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNombreCompleto(c.getNombreCompleto());
        dto.setNumeroDocumento(c.getNumeroDocumento());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());

        if (c.getTipoDocumento() != null) {
            dto.setTipoDocumento(c.getTipoDocumento().getId());
            dto.setTipoDocumentoNombre(c.getTipoDocumento().getNombre());
        }

        return dto;
    }

    // ✅ Listar todos los clientes
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Buscar por email
    public Optional<ClienteDTO> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(this::convertirADTO);
    }

    // ✅ Buscar por número de documento
    public cliente buscarPorNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento)
                 .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + numeroDocumento));
    }

    // ✅ Buscar por ID
    public Optional<ClienteDTO> obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertirADTO);
    }

    // ✅ Crear cliente con validaciones y contraseña encriptada
    public cliente crearCliente(ClienteDTO clienteDTO) {

        // 🔸 Validar duplicados por email o documento
        if (clienteRepository.findByNumeroDocumento(clienteDTO.getNumeroDocumento()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese número de documento");
        }

        if (clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }

        cliente c = new cliente();
        c.setNombreCompleto(clienteDTO.getNombreCompleto());
        c.setNumeroDocumento(clienteDTO.getNumeroDocumento());
        c.setEmail(clienteDTO.getEmail());
        c.setTelefono(clienteDTO.getTelefono());

        // 🔐 Contraseña encriptada
        String contraseniaBase = clienteDTO.getContra();
        if (!StringUtils.hasText(contraseniaBase)) {
            // Si el DTO no trae contraseña, usa el número de documento como base
            contraseniaBase = clienteDTO.getNumeroDocumento();
        }
        c.setContra(passwordEncoder.encode(contraseniaBase));

        // 🔸 Tipo de documento
            if (clienteDTO.getTipoDocumento() == null || clienteDTO.getTipoDocumento() == 0L) {
        throw new RuntimeException("Debe seleccionar un tipo de documento");
    }
    tipoDocumento tDocumento = tDocumentoRepository.findById(clienteDTO.getTipoDocumento())
            .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
    c.setTipoDocumento(tDocumento);

        // 🔸 Rol automático "CLIENTE"
        rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        c.setRol(rolCliente);

        // 🔸 Activado por defecto
        c.setEnabled(true);

        return clienteRepository.save(c);
    }

    // ✅ Actualizar cliente existente
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        c.setNombreCompleto(clienteDTO.getNombreCompleto());
        c.setNumeroDocumento(clienteDTO.getNumeroDocumento());
        c.setEmail(clienteDTO.getEmail());
        c.setTelefono(clienteDTO.getTelefono());

        // Si el DTO trae una nueva contraseña, actualizarla
        if (StringUtils.hasText(clienteDTO.getContra())) {
            c.setContra(passwordEncoder.encode(clienteDTO.getContra()));
        }

        cliente actualizado = clienteRepository.save(c);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar cliente
    public void eliminarCliente(long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("El cliente con ID " + id + " no existe");
        }
        clienteRepository.deleteById(id);
    }

    // ✅ Filtro de clientes
    public List<ClienteDTO> filtrarClientes(String buscar, Long tipoDocumentoId, String estado) {
        List<cliente> todosLosClientes = clienteRepository.findAll();

        return todosLosClientes.stream()
                .filter(c -> {
                    if (!StringUtils.hasText(buscar)) return true;
                    String busquedaLower = buscar.toLowerCase();

                    boolean coincideNombre = c.getNombreCompleto() != null &&
                            c.getNombreCompleto().toLowerCase().contains(busquedaLower);
                    boolean coincideDocumento = c.getNumeroDocumento() != null &&
                            c.getNumeroDocumento().toLowerCase().contains(busquedaLower);

                    return coincideNombre || coincideDocumento;
                })
                .filter(c -> {
                    if (tipoDocumentoId == null || tipoDocumentoId == 0) return true;
                    return c.getTipoDocumento() != null && c.getTipoDocumento().getId() == tipoDocumentoId;
                })
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Devuelve la entidad directamente
public cliente obtenerEntidadPorId(Long id) {
    return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + id));
}

}

