package com.zencart.user_service.auth.service;

import com.zencart.user_service.dto.CredentialDTO;

public interface CredentialService {
//    List<CredentialDTO> findAll();
//
//    CredentialDTO findById(Integer credentialId);

    CredentialDTO save(CredentialDTO credentialDTO);

//    CredentialDTO update(Integer credentialId, CredentialDTO credentialDTO);
//
//    void deleteById(Integer credentialId);

    CredentialDTO findByUsername( String username);
}
