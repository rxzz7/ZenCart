package com.zencart.user_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zencart.user_service.constant.AppConstant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "verification_tokens",
        indexes = @Index(name = "idx_verification_token", columnList = "verify_token", unique = true))
@Data
@EqualsAndHashCode(callSuper = true, exclude = "credential")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_token_id",nullable = false, updatable = false, unique = true)
    private Integer verificationTokenId;

    @Column(name = "verify_token")
    private String token;

    @JsonFormat(pattern = AppConstant.LOCAL_DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = AppConstant.LOCAL_DATE_FORMAT)
    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false, length = 32)
    private TokenPurpose purpose;

}
