package com.zencart.order_service.service.impl;

import com.zencart.order_service.config.JwtTokenFilter;
import com.zencart.order_service.dto.order.OrderDto;
import com.zencart.order_service.exception.OrderNotFoundException;
import com.zencart.order_service.mappers.OrderMapper;
import com.zencart.order_service.repo.OrderRepo;
import com.zencart.order_service.service.CallAPI;
import com.zencart.order_service.service.OrderService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CallAPI callAPI;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDto> findAll() {
        log.info("OrderDto List, service; fetch all orders");
        return orderRepo.findAll().stream()
                .map(OrderMapper::map)
                .peek(orderDto -> {
                    try {
                        orderDto.setProductDto(callAPI.recieveProductDto(orderDto.getProductId(), JwtTokenFilter.getTokenFromRequest()));
                    }catch (Exception e) {
                        log.error("Error fetching product info: {}", e.getMessage());
                    }
                }).toList();
    }

    @Override
    public Page<OrderDto> findAll(int page, int size, String sortBy, String sortOrder) {
        log.info("OrderDto List, service; fetch all orders with paging and sorting");
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);


        List<OrderDto> orderDtos = orderRepo.findAll(pageable)
                .stream()
                .map(OrderMapper::map)
                .peek(orderDto -> {
                    try {
                        orderDto.setProductDto(callAPI.recieveProductDto(orderDto.getProductId(), JwtTokenFilter.getTokenFromRequest()));
                    } catch (Exception e) {
                        log.error("Error fetching product info: {}", e.getMessage());
                    }
                })
                .toList();
        return new PageImpl<>(orderDtos, pageable, orderDtos.size());
    }

    @Override
    public OrderDto findById(Integer orderId) {
        log.info("OrderDto, service; fetch order by id");
        OrderDto orderDto = orderRepo.findById(orderId)
                .map(OrderMapper::map)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order with id: %d not found", orderId)));
        try {
            orderDto.setProductDto(callAPI.recieveProductDto(orderDto.getProductId(), JwtTokenFilter.getTokenFromRequest()));
        } catch (Exception e) {
            log.error("Error fetching product info: {}", e.getMessage());
        }
        return orderDto;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        log.info("OrderDto, service; save order");
        return OrderMapper.map(orderRepo.save(OrderMapper.map(orderDto)));
    }

    @Override
    public OrderDto update(Integer orderId, OrderDto orderDto) {
        log.info("OrderDto, service; update order by id");
        OrderDto existingOrder = findById(orderId);
        modelMapper.map(orderDto, existingOrder);
        existingOrder.setOrderId(orderId);
        return OrderMapper.map(orderRepo.save(OrderMapper.map(existingOrder)));
    }

    @Override
    public void deleteById(Integer orderId) {
        log.info("Void, service; Delete by id");
        orderRepo.findById(orderId).ifPresent(order -> orderRepo.deleteById(orderId));

    }

    @Override
    public Boolean existsByOrderId(Integer orderId) {
        return orderRepo.findById(orderId).isPresent();
    }
}
