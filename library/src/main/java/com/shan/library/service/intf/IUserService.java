package com.shan.library.service.intf;

import com.shan.library.entity.user.User;
import lombok.NonNull;

import java.util.UUID;

public interface IUserService {
    User create(@NonNull String email, @NonNull String username, @NonNull String password);

    User getById(@NonNull UUID id);
}
