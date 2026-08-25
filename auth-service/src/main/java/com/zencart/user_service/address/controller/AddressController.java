package com.zencart.user_service.address.controller;

import com.zencart.user_service.dto.AddressDTO;
import com.zencart.user_service.response.ResponseCollectionDTO;
import com.zencart.user_service.address.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ResponseCollectionDTO<AddressDTO>> findAll(){
        log.info("AddressController; fetch all the addresses");
        return ResponseEntity.ok(new ResponseCollectionDTO<>(addressService.findAll()));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> findById(@PathVariable("addressId")
                                                   @NotBlank(message = "Input should not be blank")
                                                   @Valid final String addressId){
        log.info("AddressController; fetch address by Id");
        return ResponseEntity.ok(addressService.findById(Integer.parseInt(addressId.strip())));
    }
    @PostMapping
    public ResponseEntity<AddressDTO> save(@RequestBody @NotNull(message = "Fields must not be empty")
                                               @Valid final AddressDTO addressDTO){
        log.info("Address Controller; save Address");
        return ResponseEntity.ok(addressService.save(addressDTO));
    }

//    @PutMapping
//    public ResponseEntity<AddressDTO> update(@RequestBody @NotNull(message = "Fields must not be empty") @Valid AddressDTO addressDTO){
//        log.info("**AddressController ; update Address");
////        return addressService.update(addressDTO);
//        return ResponseEntity.ok(addressService.update(addressDTO));
//    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> update(@PathVariable("addressId") @NotBlank(message = "Input must not be empty") @Valid Integer addressId ,
                                             @RequestBody @NotNull(message = "Fields must not be empty") @Valid AddressDTO addressDTO){
        log.info("**AddressController; Update address by id**");
        return ResponseEntity.ok(addressService.update(addressId, addressDTO));
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("addressId")
                                                  @NotBlank(message = "Input should not be blank")
                                                  @Valid final String addressId){
        log.info("** AddressController; delete address by id");
        addressService.deleteById(Integer.parseInt(addressId.strip()));
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
