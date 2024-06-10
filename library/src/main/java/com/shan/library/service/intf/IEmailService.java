package com.shan.library.service.intf;

import lombok.NonNull;

public interface IEmailService {
    void sendPasswordResetEmail(@NonNull String email, @NonNull String token);
}
