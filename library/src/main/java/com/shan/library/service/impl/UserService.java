package com.shan.library.service.impl;

import com.shan.library.entity.user.User;
import com.shan.library.exception.ConflictException;
import com.shan.library.exception.notfond.EntityNotFoundException;
import com.shan.library.repository.IRoleRepository;
import com.shan.library.repository.IUserRepository;
import com.shan.library.service.intf.IUserService;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(@NonNull String email, @NonNull String username, @NonNull String password) {
        if (userRepository.existsByEmail(email))
            throw new ConflictException("User with this email already exists");
        if (userRepository.existsByUsername(username))
            throw new ConflictException("Username already exists");
        User user = new User(email, username, passwordEncoder.encode(password));
        user.setRole(roleRepository.findByName("ROLE_PUBLISHER").orElseThrow(() ->
                new EntityNotFoundException("No role found")));
        return userRepository.save(user);
    }

    @Override
    public User getById(@NonNull UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No such user"));
    }
}
