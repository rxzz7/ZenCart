package com.zencart.user_service.repo;

import com.zencart.user_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
    void deleteAllByCredentialCredentialId(Integer credentialId);
}
