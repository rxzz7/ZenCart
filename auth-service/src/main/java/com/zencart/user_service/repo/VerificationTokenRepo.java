package com.zencart.user_service.repo;

import com.zencart.user_service.entity.TokenPurpose;
import com.zencart.user_service.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByTokenAndPurpose(String token, TokenPurpose purpose);
}
