package com.zencart.user_service.mapper;

import com.zencart.user_service.dto.AddressDTO;
import com.zencart.user_service.dto.CredentialDTO;
import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserDTO toDTO(final User user){
        if(user == null){
                return null;
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if(user.getCredential() != null){
            userDTO.setCredentialDTO(modelMapper.map(user.getCredential(), CredentialDTO.class));
        }

        if(user.getAddresses() != null){
            Set<AddressDTO> addressDTOS = user.getAddresses()
                    .stream()
                    .map(address -> modelMapper.map(address, AddressDTO.class))
                    .collect(Collectors.toSet());

            userDTO.setAddressDTOs(addressDTOS);
        }
        return userDTO;
    }

    public User toEntity(final UserDTO userDTO){
        if(userDTO == null){
            return null;
        }
        User user = modelMapper.map(userDTO,User.class);
        if(user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user)); //This keeps the bidirectional relationship consistent.
        }
        if(user.getCredential() != null){
            user.getCredential().setUser(user); //This keeps the bidirectional relationship consistent.
        }
        return user;
    }
}
