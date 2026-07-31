package com.zencart.user_service.service.serviceImpl;

import com.zencart.user_service.dto.AddressDTO;
import com.zencart.user_service.repo.AddressRepo;
import com.zencart.user_service.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepo addressRepo;

    public AddressServiceImpl(AddressRepo addressRepo){
        this.addressRepo = addressRepo;
    }

    @Override
    public List<AddressDTO> findAll() {
        return List.of();
    }

    @Override
    public AddressDTO findById(Integer userId) {
        return null;
    }

    @Override
    public AddressDTO save(AddressDTO addressDTO) {
        return null;
    }

    @Override
    public ResponseEntity<AddressDTO> update(AddressDTO addressDTO) {
        return null;
    }

    @Override
    public ResponseEntity<AddressDTO> update(Integer addressId, AddressDTO addressDTO) {
        return null;
    }

    @Override
    public void deleteById(Integer addressId) {

    }
}
