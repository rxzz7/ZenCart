package com.zencart.user_service.repo;

import com.zencart.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByCredentialUsername(final String username);
}
