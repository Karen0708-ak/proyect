package com.utc.proyect.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utc.proyect.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}