package com.backend.estudiantes.utils;

import com.backend.estudiantes.model.Usuario;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseBuilder {
    public static Map<String, Object> builErrorResponse(String message, HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        response.put("status", status.value());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

}
