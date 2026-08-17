package com.zencart.user_service.mapper;

import com.zencart.user_service.dto.AddressDTO;
import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.Address;
import com.zencart.user_service.entity.User;

public final class AddressMapping {

    public static AddressDTO toDTO(final Address address){

         UserDTO userDTO = UserDTO.builder()
                .userId(address.getUser().getUserId())
                .firstName(address.getUser().getFirstName())
                .lastName(address.getUser().getLastName())
                .imageUrl(address.getUser().getImageUrl())
                .email(address.getUser().getEmail())
                .phone(address.getUser().getPhone())
                .build();

         return AddressDTO.builder()
                 .addressId(address.getAddressId())
                 .fullAddress(address.getFullAddress())
                 .city(address.getCity())
                 .postalCode(address.getPostalCode())
                 .userDTO(userDTO)
                 .build();
    }

    public static Address toEntity(AddressDTO addressDTO){

        User user = User.builder()
                .userId(addressDTO.getUserDTO().getUserId())
                .firstName(addressDTO.getUserDTO().getFirstName())
                .lastName(addressDTO.getUserDTO().getLastName())
                .imageUrl(addressDTO.getUserDTO().getImageUrl())
                .email(addressDTO.getUserDTO().getEmail())
                .phone(addressDTO.getUserDTO().getPhone())
                .build();

        return Address.builder()
                .addressId(addressDTO.getAddressId())
                .fullAddress(addressDTO.getFullAddress())
                .postalCode(addressDTO.getPostalCode())
                .city(addressDTO.getCity())
                .user(user)
                .build();
    }
}
