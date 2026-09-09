package com.zencart.order_service.service.impl;

import com.zencart.order_service.config.JwtTokenFilter;
import com.zencart.order_service.dto.order.CartDto;
import com.zencart.order_service.entity.Cart;
import com.zencart.order_service.exception.CartNotFoundException;
import com.zencart.order_service.mappers.CartMapper;
import com.zencart.order_service.repo.CartRepo;
import com.zencart.order_service.service.CallAPI;
import com.zencart.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepo cartRepo;
    private final CallAPI callAPI;
    private final ModelMapper modelMapper;

    @Override
    public List<CartDto> findAll() {
        log.info("CartDto, List, Service; fetching all cart");
        return cartRepo.findAll().stream().map(CartMapper::map)
                .peek(cartDto -> {
            try {
                cartDto.setUserDto(callAPI.recieverUserDto(cartDto.getUserId(), JwtTokenFilter.getTokenFromRequest()));
            } catch (Exception e) {
                log.error("Error fetching user info: {}", e.getMessage());
            }
        }).toList();
    }
    @Override
    public Page<CartDto> findAll(int page, int size, String sortBy, String sortOrder){
        log.info("CartDto List, service; fetch all carts with paging and sorting");
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<CartDto> cartDtos =cartRepo.findAll(pageable)
                .stream()
                .map(CartMapper::map)
                .peek(cartDto -> {
                    try {
                        cartDto.setUserDto(callAPI.recieverUserDto(cartDto.getUserId(), JwtTokenFilter.getTokenFromRequest()));
                    } catch (Exception e) {
                        log.error("Error fetching user info: {}", e.getMessage());
                    }
                }).toList();
        return new PageImpl<>(cartDtos, pageable, cartDtos.size());

    }

    @Override
    public CartDto findById(Integer cartId) {
        log.info("CartDto, service; fetch cart by id");
        CartDto cartDto = cartRepo.findById(cartId).map(CartMapper::map)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart with id: %d not found", cartId)));
        try {
            cartDto.setUserDto(callAPI.recieverUserDto(cartDto.getUserId(), JwtTokenFilter.getTokenFromRequest()));
        }catch (Exception e) {
            log.error("Error fetching user info: {}", e.getMessage());
        }
        return cartDto;
    }

    @Override
    public CartDto save(CartDto cartDto) {
        log.info("CartDto, service; save cart");
        return modelMapper.map(cartRepo.save(modelMapper.map(cartDto, Cart.class)), CartDto.class);
    }

    @Override
    public CartDto update(Integer cartId, CartDto cartDto) {
        log.info("CartDto, service; update cart by id");
        CartDto existingCart = findById(cartId);
        modelMapper.map(cartDto, existingCart);
        existingCart.setCartId(cartId);

        return CartMapper.map(cartRepo.save(CartMapper.map(existingCart)));
    }

    @Override
    public void deleteById(Integer cartId) {
        log.info("Void, service; delete cart by id ");
        cartRepo.findById(cartId).ifPresent(cart -> cartRepo.deleteById(cartId));
    }
}
