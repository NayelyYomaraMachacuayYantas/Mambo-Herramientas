package com.sistemaFacturacion.Mambo.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaFacturacion.Mambo.Repository.ClienteRepository;
import com.sistemaFacturacion.Mambo.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.Repository.UsuarioRepository;
import com.sistemaFacturacion.Mambo.dto.AuthResponse;
import com.sistemaFacturacion.Mambo.dto.LoginRequest;
import com.sistemaFacturacion.Mambo.dto.RegisterRequest;
import com.sistemaFacturacion.Mambo.model.Usuario;
import com.sistemaFacturacion.Mambo.model.cliente;
import com.sistemaFacturacion.Mambo.model.rol; // 👈 asegúrate de usar Rol con mayúscula
import com.sistemaFacturacion.Mambo.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
         private final UsuarioRepository userRepository;
    private final ClienteRepository clienteRepository; // 👈 añadir
    private final RolRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

   public AuthResponse login(LoginRequest request) {
    // 🔹 Primero intentamos con Usuario
    Usuario user = userRepository.findByNumeroDocumento(request.getNumeroDocumento()).orElse(null);
    if (user != null) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNumeroDocumento(), request.getPassword())
        );
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

    // 🔹 Si no se encuentra en Usuario, intentamos con Cliente
    cliente c = clienteRepository.findByNumeroDocumento(request.getNumeroDocumento()).orElse(null);
    if (c != null) {
        // Validar contraseña manualmente con passwordEncoder
        if (!passwordEncoder.matches(request.getPassword(), c.getContra())) {
            throw new RuntimeException("Usuario o contraseña incorrecto");
        }
        String token = jwtService.getTokenCliente(c);
        return AuthResponse.builder().token(token).build();
    }

    // 🔹 Si no se encuentra en ninguna tabla
    throw new UsernameNotFoundException("Usuario o Cliente no encontrado");
}


    public AuthResponse register(RegisterRequest request) {
        rol userRole = roleRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en la base de datos"));

        cliente newCliente = new cliente();
        newCliente.setNumeroDocumento(request.getNumeroDocumento());
        newCliente.setContra(passwordEncoder.encode(request.getPassword()));
        newCliente.setEnabled(true);
        newCliente.setRol(userRole);
        // Aquí podrías setear otros campos como nombre, email, teléfono
        clienteRepository.save(newCliente);

        String token = jwtService.getTokenCliente(newCliente);
        return AuthResponse.builder().token(token).build();
    }
}
