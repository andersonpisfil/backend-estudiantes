package com.backend.estudiantes.controller;

import com.backend.estudiantes.dto.LoginRequest;
import com.backend.estudiantes.dto.RefreshTokenRequest;
import com.backend.estudiantes.model.RefreshToken;
import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.service.AuthService;
import com.backend.estudiantes.service.JwtService;
import com.backend.estudiantes.service.RefreshTokenService;
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

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public  AuthController(AuthService authService , JwtService jwtService, RefreshTokenService refreshTokenService){
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }


    //Endpoint de login
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.authenticate(request.getEmail(), request.getPassword());
            refreshTokenService.deleteByusuario(usuario);


            Map<String,Object> extraClaims = new HashMap<>();
            extraClaims.put("rol", usuario.getRol());
            extraClaims.put("nombre", usuario.getNombre());
            extraClaims .put("email", usuario.getEmail());

            String jwtToken = jwtService.generateToken(extraClaims, usuario);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario);

            return  ResponseEntity.ok(AuthResponseBuilder.builAuthResponse(
                    jwtToken,
                    refreshToken.getToken(),
                    usuario
            ));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(ErrorResponseBuilder.builErrorResponse(
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ));
        }

    }

    @PostMapping("/refresh-Token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        try{
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                    .orElseThrow(() -> new RuntimeException("Refresh token no es valido"));

            if (refreshTokenService.isTokenExpired(refreshToken)){
                refreshTokenService.deleteByusuario(refreshToken.getUsuario());
                throw new RuntimeException("Refresh token ha expirado");
            }
            RefreshToken newRefreshToken = refreshTokenService.rotateRefreshtoken(refreshToken);
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("rol", refreshToken.getUsuario().getRol().name());
            extraClaims.put("nombre", refreshToken.getUsuario().getNombre());

            String newJwt = jwtService.generateToken(extraClaims, refreshToken.getUsuario());
            return ResponseEntity.ok(AuthResponseBuilder.builAuthResponse(
                    newJwt,
                    newRefreshToken.getToken(),
                    refreshToken.getUsuario()
            ));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(ErrorResponseBuilder.builErrorResponse(
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ));
        }
    }
}
