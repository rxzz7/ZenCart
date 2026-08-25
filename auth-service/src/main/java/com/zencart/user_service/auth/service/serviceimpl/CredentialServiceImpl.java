package com.zencart.user_service.auth.service.serviceimpl;

import com.zencart.user_service.auth.service.CredentialService;
import com.zencart.user_service.dto.CredentialDTO;
import com.zencart.user_service.entity.Credential;
import com.zencart.user_service.entity.User;
import com.zencart.user_service.exception.UserObjectNotFoundException;
import com.zencart.user_service.mapper.CredentialMapping;
import com.zencart.user_service.mapper.UserMapper;
import com.zencart.user_service.repo.CredentialRepo;
import com.zencart.user_service.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepo credentialRepo;
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public CredentialServiceImpl(CredentialRepo credentialRepo, UserMapper userMapper, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.credentialRepo = credentialRepo;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }




    @Override
    public CredentialDTO save(CredentialDTO credentialDTO) {
        log.info("CredentialDTO; save credential");

        if(credentialDTO.getPassword() == null || credentialDTO.getPassword().isBlank()){
            throw new IllegalArgumentException("Password is required");
        }
        credentialDTO.setPassword(passwordEncoder.encode(credentialDTO.getPassword()));

        //...
                User user = userRepo.findById(credentialDTO.getUserDTO().getUserId()).
                orElseThrow(()->new UserObjectNotFoundException("User not found "));
        Credential credential = getCredential(credentialDTO, user);

        Credential savedCredential = credentialRepo.save(credential);
        return CredentialMapping.toDTO(savedCredential);
    }

    @Override
    public CredentialDTO findByUsername(String username) {
        log.info("CredentialDTO; find credential by username");
        return CredentialMapping.toDTO(credentialRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UserObjectNotFoundException(String.format("Credential with username %s not found", username))));
    }

    private static Credential getCredential(CredentialDTO credentialDTO, User user) {
        Credential credential = new Credential();
        credential.setUser(user);
        credential.setUsername(credentialDTO.getUsername());
        credential.setPassword(credentialDTO.getPassword());
        credential.setRoleBasedAuthority(credentialDTO.getRoleBasedAuthority());
        credential.setIsEnabled(credentialDTO.getIsEnabled());
        credential.setIsCredentialsNonExpired(credentialDTO.getIsCredentialsNonExpired());
        credential.setIsAccountNonExpired(credentialDTO.getIsAccountNonExpired());
        credential.setIsAccountNonLocked(credentialDTO.getIsAccountNonLocked());
        return credential;
    }
//
//    @Override
//    public List<CredentialDTO> findAll() {
//        log.info("CredentialDTO; find all elements");
//        return credentialRepo.findAll().stream()
//                .map(CredentialMapping::toDTO)
//                .distinct()
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public CredentialDTO findById(Integer credentialId) {
//        log.info("CredentialDTO; find credential by Id");
//        return credentialRepo.findById(credentialId)
//                .map(CredentialMapping::toDTO)
//                .orElseThrow(() ->
//                        new CredentialNotFoundException(String.format("Credential not found of this %d",credentialId)));
//    }
//

//
//    @Override
//    public CredentialDTO update(Integer credentialId, CredentialDTO credentialDTO) {
//        log.info("Updating credential with id {}", credentialId);
//
//        Credential existingCredential = credentialRepo.findById(credentialId)
//                .orElseThrow(()->
//                new CredentialNotFoundException(String.format("Credential not found of this %d",credentialId)));
//
//        existingCredential.setUsername(credentialDTO.getUsername());
//        existingCredential.setPassword(credentialDTO.getPassword());
//        existingCredential.setRoleBasedAuthority(credentialDTO.getRoleBasedAuthority());
//        existingCredential.setIsEnabled(credentialDTO.getIsEnabled());
//        existingCredential.setIsCredentialsNonExpired(credentialDTO.getIsCredentialsNonExpired());
//        existingCredential.setIsAccountNonExpired(credentialDTO.getIsAccountNonExpired());
//        existingCredential.setIsAccountNonLocked(credentialDTO.getIsAccountNonLocked());
//
//        if(credentialDTO.getUserDTO() != null){
//            existingCredential.setUser(userMapper.toEntity(credentialDTO.getUserDTO()));
//        }
//        Credential updatedCredential = credentialRepo.save(existingCredential);
//        return CredentialMapping.toDTO(updatedCredential);
//    }
//
//    @Override
//    public void deleteById(Integer credentialId) {
//        log.info("**Delete credential by Id");
//        credentialRepo.deleteById(credentialId);
//    }


}
