package com.zencart.payment_service.service.impl;

import com.zencart.payment_service.config.JwtTokenFilter;
import com.zencart.payment_service.dto.OrderDto;
import com.zencart.payment_service.dto.PaymentDto;
import com.zencart.payment_service.dto.UserDto;
import com.zencart.payment_service.exception.PaymentNotFoundException;
import com.zencart.payment_service.mapper.PaymentMapper;
import com.zencart.payment_service.repo.PaymentRepo;
import com.zencart.payment_service.service.CallAPI;
import com.zencart.payment_service.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;
    private final CallAPI callAPI;


    @Override
    public List<PaymentDto> findAll() {
        log.info("PaymentDto List, service; fetch all payments");
        return paymentRepo.findAll().stream()
                .map(PaymentMapper::map)
                .peek(paymentDto -> {
                    try {
                        String token = JwtTokenFilter.getTokenFromRequest();
                        OrderDto orderDto = callAPI.receiverPaymentDto(paymentDto.getOrderId(), token);
                        if(orderDto != null){
                            paymentDto.setOrderDto(modelMapper.map(orderDto, OrderDto.class));
                        }
                        UserDto userDto = callAPI.receiverUserDto(paymentDto.getUserId(), token);
                        if (userDto != null){
                            paymentDto.setUserDto(userDto);
                        }
                    } catch (Exception e) {
                        log.error("Error fetching order info for payment {}: {}", paymentDto.getPaymentId(), e.getMessage());
                    }
                    //userDto
                }).toList();
    }

    @Override
    public Page<PaymentDto> findAll(int page, int size, String sortBy, String sortOrder) {
        log.info("PaymentDto List, service; fetch all payments with paging");
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<PaymentDto> paymentDtos = paymentRepo.findAll(pageable)
                .map(PaymentMapper::map)
                .stream()
                .peek(paymentDto -> {
                    try {
                        String token = JwtTokenFilter.getTokenFromRequest();
                        OrderDto orderDto = callAPI.receiverPaymentDto(paymentDto.getOrderId(),token);
                        if (orderDto != null) {
                            paymentDto.setOrderDto(modelMapper.map(orderDto, OrderDto.class));
                        }
                        UserDto userDto = callAPI.receiverUserDto(paymentDto.getUserId(), token);
                        if (userDto != null){
                            paymentDto.setUserDto(userDto);
                        }
                    } catch (Exception e) {
                        log.error("Error fetching order info: {}", e.getMessage());
                    }
                    //userDto
                })
                .toList();
        return new PageImpl<>(paymentDtos, pageable, paymentDtos.size());
    }

    @Override
    public PaymentDto findById(Integer paymentId) {
        PaymentDto paymentDto = paymentRepo.findById(paymentId)
                .map(PaymentMapper::map)
                .orElseThrow(() -> new PaymentNotFoundException(String.format("Payment with id: %d not found", paymentId)));
        try {
            String token = JwtTokenFilter.getTokenFromRequest();
            OrderDto orderDto = callAPI.receiverPaymentDto(paymentDto.getOrderId(), token);
            if(orderDto != null){
                paymentDto.setOrderDto(orderDto);
            }
            UserDto userDto = callAPI.receiverUserDto(paymentDto.getUserId(), token);
            if (userDto != null){
                paymentDto.setUserDto(userDto);
            }
        }catch (Exception e) {
            log.error("Error fetching order or user info: {}", e.getMessage());
        }
        return paymentDto;
    }

    @Override
    public PaymentDto save(PaymentDto paymentDto) {
        log.info("PaymentDto , Service; save payment");
        if (paymentRepo.existsByOrderIdAndIsPayed(paymentDto.getOrderId())) {
            throw new PaymentNotFoundException("Order has already been paid.");
        }
        PaymentDto savedPaymentDto = PaymentMapper.map(paymentRepo.save(PaymentMapper.map(paymentDto)));
        try {
            String token = JwtTokenFilter.getTokenFromRequest();
            savedPaymentDto.setOrderDto(callAPI.receiverPaymentDto(paymentDto.getOrderId(), token));
            savedPaymentDto.setUserDto(callAPI.receiverUserDto(paymentDto.getUserId(), token));
        }catch (Exception e) {
            log.error("Error fetching order or user info: {}", e.getMessage());
        }
         //
         //
         //
         //
        return savedPaymentDto;
    }

    @Override
    public PaymentDto update(PaymentDto paymentDto) {
        log.info("PaymentDto, service; update payment");
        return PaymentMapper.map(paymentRepo.save(PaymentMapper.map(paymentDto)));
    }

    @Override
    public PaymentDto update(Integer paymentId, PaymentDto paymentDto) {
        log.info("PaymentDto, service; update payment by id");
        PaymentDto existingPaymentDto = findById(paymentId);
        modelMapper.map(paymentDto, existingPaymentDto);
        existingPaymentDto.setPaymentId(paymentId);
        return PaymentMapper.map(paymentRepo.save(PaymentMapper.map(existingPaymentDto)));
    }

    @Override
    public void deleteById(Integer paymentId) {
        log.info("Void, Service; Delete payment by id");
        paymentRepo.findById(paymentId).ifPresent(payment-> paymentRepo.deleteById(paymentId));

    }

    public OrderDto getOrderDto(Integer orderId){
        return callAPI.receiverPaymentDto(orderId, JwtTokenFilter.getTokenFromRequest());
    }
}
