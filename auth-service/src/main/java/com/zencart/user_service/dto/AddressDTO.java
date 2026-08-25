package com.zencart.user_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO implements Serializable {

    private static long serialVersionUID = 1L;

    private Integer addressId;

    private String fullAddress;

    private String postalCode;

    private String  city;

    @JsonProperty("user")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private UserDTO userDTO;
}
