package com.zencart.order_service.dto.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zencart.order_service.dto.order.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @JsonProperty("category")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDto categoryDto;

    @JsonProperty("order")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderDto orderDto;
}
