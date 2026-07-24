package com.backend.estudiantes.service;


import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UsuarioDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioDetailsService.class);
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email {}", email);
                    return new UsernameNotFoundException("Usuario NO Encontrado!");
                });
        log.info("Usuario encontrado {} con rol: {} ", email, usuario.getRol());

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}
