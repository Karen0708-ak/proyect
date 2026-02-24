package com.utc.proyect.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.utc.proyect.entity.Usuario;
import com.utc.proyect.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rolNormalizado = usuario.getRol() == null
                ? "USER"
                : usuario.getRol().replaceFirst("(?i)^ROLE_", "").trim().toUpperCase(Locale.ROOT);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(rolNormalizado)
                .build();
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Solicitud de registro invalida.");
        }

        String username = usuario.getUsername() == null ? "" : usuario.getUsername().trim();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contrasena es obligatoria.");
        }

        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("El nombre de usuario ya existe.");
        }

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("ROLE_USER");
        usuarioRepository.save(usuario);
    }
}
