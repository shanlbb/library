package com.shan.library.service.impl;

import com.shan.library.entity.user.RefreshToken;
import com.shan.library.entity.user.User;
import com.shan.library.exception.UnauthorizedException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IRefreshTokenRepository;
import com.shan.library.repository.IUserRepository;
import com.shan.library.service.intf.IEmailService;
import com.shan.library.service.intf.IResetPasswordService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetPasswordService implements IResetPasswordService {

    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.password.reset.token.lifetime}")
    private Duration tokenLifetime;

    public ResetPasswordService(IUserRepository userRepository, IRefreshTokenRepository refreshTokenRepository,
                                IEmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendPasswordResetLink(@NonNull String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRefreshToken() != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
        RefreshToken refreshToken = new RefreshToken(LocalDateTime.now().plus(tokenLifetime), user);
        refreshTokenRepository.save(refreshToken);
        emailService.sendPasswordResetEmail(email, refreshToken.getId().toString());
    }

    @Override
    public void resetPassword(@NonNull String token, @NonNull String password) {
        RefreshToken refreshToken = refreshTokenRepository.findById(UUID.fromString(token)).orElseThrow(() ->
                new EntityNotFoundException("Refresh token not found"));
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("Token lifetime has expired");
        User user = refreshToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}
