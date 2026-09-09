package com.zencart.order_service.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zencart.order_service.dto.user.UserDto;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer cartId;
    private Integer userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<OrderDto> orderDtos;

    @JsonProperty("user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto userDto;

}
