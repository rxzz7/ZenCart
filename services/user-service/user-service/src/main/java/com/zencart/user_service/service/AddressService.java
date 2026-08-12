package com.zencart.user_service.service;

import com.zencart.user_service.dto.AddressDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    List<AddressDTO> findAll();
    AddressDTO findById(final Integer addressId);
    AddressDTO save(final AddressDTO addressDTO);
    AddressDTO update(final Integer addressId, final AddressDTO addressDTO);
    void deleteById(final Integer addressId);
}
