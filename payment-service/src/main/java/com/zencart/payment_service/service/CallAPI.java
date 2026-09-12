package com.zencart.payment_service.service;

import com.zencart.payment_service.dto.OrderDto;
import com.zencart.payment_service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CallAPI {

    private final RestClient.Builder restClientBuilder;

    public OrderDto receiverPaymentDto(Integer orderId, String token){
        return restClientBuilder.baseUrl("http://localhost:8083").build()
                .get()
                .uri("/api/orders/{orderId}", orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(OrderDto.class);
    }

    public UserDto receiverUserDto(Integer userId, String token){
        return restClientBuilder.baseUrl("http://localhost:8081").build()
                .get()
                .uri("/api/users/{userId}",userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(UserDto.class);
    }
}
