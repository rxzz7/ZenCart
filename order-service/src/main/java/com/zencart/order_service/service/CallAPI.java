package com.zencart.order_service.service;

import com.zencart.order_service.dto.product.ProductDto;
import com.zencart.order_service.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CallAPI {
        private final RestClient.Builder restClientBuilder;

        public UserDto recieverUserDto(Integer userId, String token){
            return restClientBuilder.baseUrl("http://localhost:8081").build()
                    .get()
                    .uri("/api/users/{userId}", userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(UserDto.class);
        }

        public ProductDto recieveProductDto(Integer productId, String token){
            return restClientBuilder.baseUrl("http://localhost:8082").build()
                    .get()
                    .uri("/api/products/{productId}", productId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(ProductDto.class);
        }


}
