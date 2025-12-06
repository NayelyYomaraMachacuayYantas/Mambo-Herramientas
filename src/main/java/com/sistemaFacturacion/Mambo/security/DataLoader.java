package com.sistemaFacturacion.Mambo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sistemaFacturacion.Mambo.model.Usuario;
import com.sistemaFacturacion.Mambo.model.rol;
import com.sistemaFacturacion.Mambo.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.Repository.UsuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            rol rolAdmin = new rol();
            rolAdmin.setNombre("ROLE_ADMIN");
            rolRepository.save(rolAdmin);

            rol rolVendedor = new rol();
            rolVendedor.setNombre("ROLE_VENDEDOR");
            rolRepository.save(rolVendedor);

            rol rolCliente = new rol();
            rolCliente.setNombre("ROLE_CLIENTE");
            rolRepository.save(rolCliente);

            System.out.println("Roles creados correctamente");
        }

        // Crear usuario ADMIN por defecto si no existe
        if (usuarioRepository.count() == 0) {
            rol adminRol = rolRepository.findByNombre("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador General");
            admin.setRol(adminRol);
            admin.setNumeroDocumento("99999999"); // DNI por defecto
            admin.setEmail("admin@mambo.com");
            admin.setTelefono("987654321");
            admin.setContra(passwordEncoder.encode("admin123")); // contraseña encriptada
            admin.setEnabled(true);
            usuarioRepository.save(admin);

            System.out.println("✅ Usuario ADMIN por defecto creado: DNI=99999999, contraseña=admin123");
        }
    }
}
