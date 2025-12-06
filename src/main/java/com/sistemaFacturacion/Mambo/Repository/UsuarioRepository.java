package com.sistemaFacturacion.Mambo.Repository;

import com.sistemaFacturacion.Mambo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 📌 Indica que esta interfaz es un repositorio de acceso a BD
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 📌 Buscar usuario por email (para login)
    Optional<Usuario> findByEmail(String email);

    // 📌 Buscar usuario por número de documento
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    //Para listar con la base de datos
    List<Usuario> findByRolNombre(String nombreRol);

    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'VENDEDOR'")
    List<Usuario> listarVendedores();

}
