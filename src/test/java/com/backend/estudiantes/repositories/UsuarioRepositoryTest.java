package com.backend.estudiantes.repositories;

import com.backend.estudiantes.model.Role;
import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void findByEmail_UsuarioExiste_RetornaUsuario(){

        //costrucctor usuario
        Usuario testUser = new Usuario(
                "Test",
                "User",
                "test@upn.pe",
                "123",
                Role.ESTUDIANTE

        );

        //Guardando usuario temporalmente
        Usuario guardado = repository.save(testUser);

        //Estamos haciendo la busqueda y devolviendolo en optinal
        Optional<Usuario>encontradoOptional = repository.findByEmail(guardado.getEmail());

        //Validando por si falla
        assertTrue(encontradoOptional.isPresent(),"El usuario deberia existir en la base de datos");

        //Obtener realmente el usuario
        Usuario encontrado = encontradoOptional.get();

        //Comparando emails a ver si coinciden
        assertEquals("test@upn.pe", encontrado.getEmail(), "El email del usuario encontrado deberia" +
                "coincidir con el de prueba");

    }
}
