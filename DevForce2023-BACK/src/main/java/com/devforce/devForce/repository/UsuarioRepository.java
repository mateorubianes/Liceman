package com.devforce.devForce.repository;

import com.devforce.devForce.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findById(long id);
    Optional<Usuario> findByUsername(String Username);
    Usuario findByNombreAndApellido(String nombre, String apellido);

    Usuario findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String mail);
}
