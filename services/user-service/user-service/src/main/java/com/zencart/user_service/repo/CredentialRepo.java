package com.zencart.user_service.repo;

import com.zencart.user_service.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepo extends JpaRepository<Credential, Integer> {

    Optional<Credential> findByUsername(final String username);
}
