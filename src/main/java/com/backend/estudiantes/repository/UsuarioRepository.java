package com.backend.estudiantes.repository;

import com.backend.estudiantes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /*
    Optional es una clase de java que va a almacenar un doto o no almacenara nada
    Como FindByEmail no siempre va a tener un usurio es necesario utilizar el OPTIONAL
     */
    Optional<Usuario> findByEmail(String email);
}
