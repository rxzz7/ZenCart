package com.zencart.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ProductDto implements Serializable {


    @Serial
    private final static long serialVersionUID = 1L;

    private Integer productId;
    private String productTitle;
    private String imageUrl;
    private String sku;
    @JsonProperty("price")
    private Double priceUnit;
    private Integer quantity;
}
