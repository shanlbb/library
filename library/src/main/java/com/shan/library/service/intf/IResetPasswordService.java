package com.shan.library.service.intf;

import lombok.NonNull;

public interface IResetPasswordService {
    void sendPasswordResetLink(@NonNull String email);
    void resetPassword(@NonNull String token, @NonNull String password);
}
