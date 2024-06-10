package com.shan.library.service.intf;

import com.shan.library.dto.user.TokensDTO;
import lombok.NonNull;

import java.util.UUID;

public interface IAuthenticationService {
    TokensDTO login(@NonNull String email, @NonNull String password);

    TokensDTO refresh(@NonNull UUID refreshToken);

    void logout(@NonNull UUID userId);
}
