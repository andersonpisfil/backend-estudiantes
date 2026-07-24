package com.backend.estudiantes.service;

import com.backend.estudiantes.model.RefreshToken;
import com.backend.estudiantes.model.Usuario;
import com.backend.estudiantes.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private Long refreshTokenDurationMs = 86400000L;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken (Usuario usuario){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken =  refreshTokenRepository.save(refreshToken);

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken (String token){
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByusuario(Usuario usuario){
        refreshTokenRepository.deleteByUsuario(usuario);
    }
    @Transactional
    public RefreshToken rotateRefreshtoken(RefreshToken oldToken){
        refreshTokenRepository.delete(oldToken);
        return createRefreshToken(oldToken.getUsuario());
    }
    public void cleanupEpiredTokens(){
        refreshTokenRepository.deleteExpiredToken();
    }

    public boolean isTokenExpired (RefreshToken token){
        return token.isExpired();
    }

}
