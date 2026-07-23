package com.backend.estudiantes.utils;

import com.backend.estudiantes.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class AuthResponseBuilder {
    public static Map<String, Object> builAuthResponse(String token, Usuario usuario){
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", builUsuarioResponse(usuario));
        return response;
    }

    private static Map<String, Object> builUsuarioResponse(Usuario usuario){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", usuario.getId());
        userMap.put("email", usuario.getEmail());
        userMap.put("rol", usuario.getRol());
        userMap.put("name", usuario.getNombre());
        userMap.put("apellido", usuario.getApellido());
        return userMap;
    }

}
