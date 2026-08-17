package com.zencart.user_service.service.serviceImpl;

import com.zencart.user_service.dto.CredentialDTO;
import com.zencart.user_service.entity.Credential;
import com.zencart.user_service.exception.CredentialNotFoundException;
import com.zencart.user_service.exception.UserObjectNotFoundException;
import com.zencart.user_service.mapper.CredentialMapping;
import com.zencart.user_service.mapper.UserMapper;
import com.zencart.user_service.repo.CredentialRepo;
import com.zencart.user_service.service.CredentialService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CredentialServiceImpl implements CredentialService {

    private CredentialRepo credentialRepo;

    private UserMapper userMapper;

    public CredentialServiceImpl(CredentialRepo credentialRepo, UserMapper userMapper) {
        this.credentialRepo = credentialRepo;
        this.userMapper = userMapper;
    }


    @Override
    public List<CredentialDTO> findAll() {
        log.info("CredentialDTO; find all elements");
        return credentialRepo.findAll().stream()
                .map(CredentialMapping::toDTO)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public CredentialDTO findById(Integer credentialId) {
        log.info("CredentialDTO; find credential by Id");
        return credentialRepo.findById(credentialId)
                .map(CredentialMapping::toDTO)
                .orElseThrow(() ->
                        new CredentialNotFoundException(String.format("Credential not found of this %d",credentialId)));
    }

    @Override
    public CredentialDTO save(CredentialDTO credentialDTO) {
        log.info("CredentialDTO; save credential");
        return CredentialMapping.toDTO(credentialRepo.save(CredentialMapping.toEntity(credentialDTO)));
    }

    @Override
    public CredentialDTO update(Integer credentialId, CredentialDTO credentialDTO) {
        log.info("Updating credential with id {}", credentialId);

        Credential existingCredential = credentialRepo.findById(credentialId)
                .orElseThrow(()->
                new CredentialNotFoundException(String.format("Credential not found of this %d",credentialId)));

        existingCredential.setUsername(credentialDTO.getUsername());
        existingCredential.setPassword(credentialDTO.getPassword());
        existingCredential.setRoleBasedAuthority(credentialDTO.getRoleBasedAuthority());
        existingCredential.setIsEnabled(credentialDTO.getIsEnabled());
        existingCredential.setIsCredentialsNonExpired(credentialDTO.getIsCredentialsNonExpired());
        existingCredential.setIsAccountNonExpired(credentialDTO.getIsAccountNonExpired());
        existingCredential.setIsAccountNonLocked(credentialDTO.getIsAccountNonLocked());

        if(credentialDTO.getUserDTO() != null){
            existingCredential.setUser(userMapper.toEntity(credentialDTO.getUserDTO()));
        }
        Credential updatedCredential = credentialRepo.save(existingCredential);
        return CredentialMapping.toDTO(updatedCredential);
    }

    @Override
    public void deleteById(Integer credentialId) {
        log.info("**Delete credential by Id");
        credentialRepo.deleteById(credentialId);
    }

    @Override
    public CredentialDTO findByUsername(String username) {
        log.info("CredentialDTO; find credential by username");
        return CredentialMapping.toDTO(credentialRepo.findByUsername(username)
                .orElseThrow(() ->
                new UserObjectNotFoundException(String.format("Credential with username %s not found", username))));
    }
}
