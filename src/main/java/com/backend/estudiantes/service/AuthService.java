package com.backend.estudiantes.service;

import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario authenticate(String email, String password){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //primero se guarda contraseña hasheada
        if(!passwordEncoder.matches(password, usuario.getPassword())){
            throw  new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }

}
