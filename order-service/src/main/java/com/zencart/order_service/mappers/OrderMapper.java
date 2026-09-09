package com.zencart.order_service.mappers;

import com.zencart.order_service.dto.order.CartDto;
import com.zencart.order_service.dto.order.OrderDto;
import com.zencart.order_service.entity.Cart;
import com.zencart.order_service.entity.Order;

public interface OrderMapper {

    static OrderDto map(Order order){

        if (order == null) return null;

        CartDto cartDto = CartDto.builder()
                .cartId(order.getCart().getCartId())
                .userId(order.getCart().getUserId())
                .build();

        return OrderDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderDesc(order.getOrderDesc())
                .orderFee(order.getOrderFee())
                .productId(order.getProductId())
                .cartDto(cartDto)
                .build();
    }

    static Order map(OrderDto orderDto){
        if (orderDto == null) return  null;

        Cart cart = Cart.builder()
                .cartId(orderDto.getCartDto().getCartId())
                .userId(orderDto.getCartDto().getUserId())
                .build();

        return Order.builder()
                .orderId(orderDto.getOrderId())
                .orderDate(orderDto.getOrderDate())
                .orderDesc(orderDto.getOrderDesc())
                .orderFee(orderDto.getOrderFee())
                .productId(orderDto.getProductId())
                .cart(cart)
                .build();
    }

}
