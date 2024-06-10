package com.shan.library.service.impl;

import com.shan.library.dto.user.TokensDTO;
import com.shan.library.entity.user.RefreshToken;
import com.shan.library.entity.user.User;
import com.shan.library.exception.UnauthorizedException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IRefreshTokenRepository;
import com.shan.library.repository.IUserRepository;
import com.shan.library.service.intf.IAuthenticationService;
import com.shan.library.util.JwtTokenProvider;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Value("${app.jwt.refresh.lifetime}")
    private Duration tokenLifetime;

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    protected AuthenticationService(PasswordEncoder passwordEncoder, IUserRepository userRepository,
                                    IRefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public TokensDTO login(@NonNull String email, @NonNull String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User with email " + email + " not found"));
        if (!passwordEncoder.matches(password, user.getPasswordHash()))
            throw new UnauthorizedException("Wrong password");
        if (user.getRefreshToken() != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
        return this.createTokens(user);
    }

    @Override
    public TokensDTO refresh(@NonNull UUID refreshToken) {
        RefreshToken rt = refreshTokenRepository.findById(refreshToken).orElseThrow(() ->
                new EntityNotFoundException("Refresh token " + refreshToken + " not found"));
        if (rt.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("Token lifetime has expired");
        User user = rt.getUser();
        refreshTokenRepository.delete(rt);
        return this.createTokens(user);
    }

    @Override
    public void logout(@NonNull UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found"));
        if (user.getRefreshToken() != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }

    private TokensDTO createTokens(User user) {
        RefreshToken refreshToken = new RefreshToken(LocalDateTime.now().plus(tokenLifetime), user);
        refreshTokenRepository.save(refreshToken);
        String accessToken = jwtTokenProvider.generateToken(new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPasswordHash(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()))
        ));
        return new TokensDTO(refreshToken.getId().toString(), accessToken);
    }
}
