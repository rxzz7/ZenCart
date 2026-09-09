package com.zencart.order_service.mappers;

import com.zencart.order_service.dto.order.CartDto;
import com.zencart.order_service.dto.order.OrderDto;
import com.zencart.order_service.entity.Cart;
import com.zencart.order_service.entity.Order;
import com.zencart.order_service.dto.user.UserDto;

import java.util.Set;
import java.util.stream.Collectors;

public interface CartMapper {

    static CartDto map(Cart cart){
        if(cart == null) return null;

        Set<OrderDto> orderDto =cart.getOrders().stream()
                        .map(order -> OrderDto.builder()
                                .orderId(order.getOrderId())
                                .orderDate(order.getOrderDate())
                                .orderDesc(order.getOrderDesc())
                                .orderFee(order.getOrderFee())
                                .build())
                        .collect(Collectors.toSet());

        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .orderDtos(orderDto)
                .userDto(
                        UserDto.builder()
                                .userId(cart.getUserId())
                                .build()
                )
                .build();

    }

    static Cart map(CartDto cartDto){
        if (cartDto == null) return null;

        Set<Order> orders = cartDto.getOrderDtos().stream()
                .map(order -> Order.builder()
                        .orderId(order.getOrderId())
                        .orderDate(order.getOrderDate())
                        .orderDesc(order.getOrderDesc())
                        .orderFee(order.getOrderFee())
                        .build())
                .collect(Collectors.toSet());

        return Cart.builder()
                .cartId(cartDto.getCartId())
                .userId(cartDto.getUserId())
                .orders(orders)
                .build();
    }
}
