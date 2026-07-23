package com.backend.estudiantes.controller;

import com.backend.estudiantes.dto.LoginRequest;
import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.service.AuthService;
import com.backend.estudiantes.service.JwtService;
import com.backend.estudiantes.utils.AuthResponseBuilder;
import com.backend.estudiantes.utils.ErrorResponseBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    public  AuthController(AuthService authService , JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }


    //Endpoint de login
    @PostMapping("login")
    public ResponseEntity<?> login(
            @Valid
            @RequestBody LoginRequest request
    ) {
        try {
            Usuario usuario = authService.authenticate(request.getEmail(), request.getPassword());

            Map<String,Object> extraClaims = new HashMap<>();
            extraClaims.put("rol", usuario.getRol());
            extraClaims.put("nombre", usuario.getNombre());
            extraClaims .put("email", usuario.getEmail());

            String jwtToken = jwtService.generateToken(extraClaims, usuario);

            return  ResponseEntity.ok(AuthResponseBuilder.builAuthResponse(jwtToken, usuario));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(ErrorResponseBuilder.builErrorResponse(
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ));
        }

    }
}
