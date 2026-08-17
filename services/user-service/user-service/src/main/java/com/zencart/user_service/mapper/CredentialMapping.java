package com.zencart.user_service.mapper;

import com.zencart.user_service.dto.CredentialDTO;
import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.Credential;
import com.zencart.user_service.entity.User;

public class CredentialMapping {

    public static CredentialDTO toDTO(Credential credential){

        UserDTO userDTO = UserDTO.builder()
                .userId(credential.getUser().getUserId())
                .firstName(credential.getUser().getFirstName())
                .lastName(credential.getUser().getLastName())
                .imageUrl(credential.getUser().getImageUrl())
                .phone(credential.getUser().getPhone())
                .email(credential.getUser().getEmail())
                .build();
        return CredentialDTO.builder()
                .credentialId(credential.getCredentialId())
                .username(credential.getUsername())
                .password(credential.getPassword())
                .roleBasedAuthority(credential.getRoleBasedAuthority())
                .isEnabled(credential.getIsEnabled())
                .isAccountNonExpired(credential.getIsAccountNonExpired())
                .isAccountNonLocked(credential.getIsAccountNonLocked())
                .isCredentialsNonExpired(credential.getIsCredentialsNonExpired())
                .userDTO(userDTO)
                .build();
    }
    public static Credential toEntity(CredentialDTO credentialDTO){

        User user = User.builder()
                .userId(credentialDTO.getUserDTO().getUserId())
                .firstName(credentialDTO.getUserDTO().getFirstName())
                .lastName(credentialDTO.getUserDTO().getLastName())
                .imageUrl(credentialDTO.getUserDTO().getImageUrl())
                .phone(credentialDTO.getUserDTO().getPhone())
                .email(credentialDTO.getUserDTO().getEmail())
                .build();

        return Credential.builder()
                .credentialId(credentialDTO.getCredentialId())
                .username(credentialDTO.getUsername())
                .password(credentialDTO.getPassword())
                .roleBasedAuthority(credentialDTO.getRoleBasedAuthority())
                .isEnabled(credentialDTO.getIsEnabled())
                .isAccountNonExpired(credentialDTO.getIsAccountNonExpired())
                .isAccountNonLocked(credentialDTO.getIsAccountNonLocked())
                .isCredentialsNonExpired(credentialDTO.getIsCredentialsNonExpired())
                .user(user)
                .build();
    }



}
