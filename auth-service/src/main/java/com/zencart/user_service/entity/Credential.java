package com.zencart.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Table(name = "credentials" ,
        uniqueConstraints = @UniqueConstraint(name = "uk_credentials_user_id", columnNames = "user_id"))
@EqualsAndHashCode(callSuper = true, exclude = {"user", "verificationTokens", "refreshTokens"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id", nullable = false, unique = true, updatable = false)
    private Integer credentialId;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private RoleBasedAuthority roleBasedAuthority = RoleBasedAuthority.ROLE_USER;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = false;

    @Column(name = "is_account_non_locked", nullable = false)
    @Builder.Default
    private Boolean isAccountNonLocked = true;

    @Column(name = "is_account_non_expired", nullable = false)
    @Builder.Default
    private Boolean isAccountNonExpired = true;

    @Column(name = "is_credentials_non_expired", nullable = false)
    @Builder.Default
    private Boolean isCredentialsNonExpired = true;



    @JsonIgnore
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "credential")
    @Builder.Default
    private Set<VerificationToken> verificationTokens = new HashSet<>();

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RefreshToken> refreshTokens = new HashSet<>();



}
