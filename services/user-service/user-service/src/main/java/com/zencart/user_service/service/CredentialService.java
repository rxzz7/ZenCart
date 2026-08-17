package com.zencart.user_service.service;

import com.zencart.user_service.dto.CredentialDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface CredentialService {
    List<CredentialDTO> findAll();

    CredentialDTO findById(Integer credentialId);

    CredentialDTO save(CredentialDTO credentialDTO);

    CredentialDTO update(Integer credentialId, CredentialDTO credentialDTO);

    void deleteById(Integer credentialId);

    CredentialDTO findByUsername( String username);
}
