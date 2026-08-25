package com.zencart.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zencart.user_service.constant.AppConstant;
import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VerificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer verificationTokenId;
    private String token;

    @JsonFormat(pattern = AppConstant.LOCAL_DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = AppConstant.LOCAL_DATE_FORMAT)
    private LocalDateTime expireDate;

    @JsonProperty("credential")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CredentialDTO credentialDTO;
}
