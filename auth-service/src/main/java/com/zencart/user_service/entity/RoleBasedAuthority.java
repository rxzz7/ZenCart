package com.zencart.user_service.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum RoleBasedAuthority {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    @Column(name = "role")
    private final String role;

}
