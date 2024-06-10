package com.shan.library.repository;

import com.shan.library.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
