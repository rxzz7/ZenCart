package com.zencart.user_service.address.service;

import com.zencart.user_service.dto.AddressDTO;
import com.zencart.user_service.entity.Address;
import com.zencart.user_service.exception.AddressNotFoundException;
import com.zencart.user_service.mapper.AddressMapping;
import com.zencart.user_service.mapper.UserMapper;
import com.zencart.user_service.repo.AddressRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserMapper userMapper ;

    public AddressServiceImpl(AddressRepo addressRepo, UserMapper userMapper){
        this.addressRepo = addressRepo;
        this.userMapper = userMapper;
    }

    @Override
    public List<AddressDTO> findAll() {

        log.info("** AddressDTO , find all addresses **");
        return addressRepo.findAll().stream()
                .map(AddressMapping::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public AddressDTO findById(Integer addressId) {
        log.info("**AdrresDTO, finding user by id **");
        return addressRepo.findById(addressId).map(AddressMapping::toDTO)
                .orElseThrow(()-> new AddressNotFoundException(String.format("Address with id %d not found", addressId)));
    }

    @Override
    public AddressDTO save(AddressDTO addressDTO) {

        log.info("**AddressDTO, saving address entity");
        return AddressMapping.toDTO(this.addressRepo.save(AddressMapping.toEntity(addressDTO)));
    }

    @Override
    public AddressDTO update(Integer addressId, AddressDTO addressDTO) {
        log.info("Updating address with id {}", addressId);

        Address existingAddress = addressRepo.findById(addressId).orElseThrow(()->
                new AddressNotFoundException(String.format("Address with id %d not found", addressId)));

        existingAddress.setFullAddress(addressDTO.getFullAddress());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setPostalCode(addressDTO.getPostalCode());

        if (addressDTO.getUserDTO() != null){
            existingAddress.setUser(userMapper.toEntity(addressDTO.getUserDTO()));
        }

        Address updatedAddress = addressRepo.save(existingAddress);
        return AddressMapping.toDTO(updatedAddress);
    }


    @Override
    public void deleteById(Integer addressId) {
        log.info("*AddressDTO , deleting the address");
        addressRepo.deleteById(addressId);
    }
}
