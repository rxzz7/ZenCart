package com.zencart.payment_service.controller;

import com.zencart.payment_service.dto.OrderDto;
import com.zencart.payment_service.dto.PaymentDto;
import com.zencart.payment_service.service.PaymentService;
import com.zencart.payment_service.service.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;
    private final PaymentServiceImpl paymentServiceImpl;

    //After changing the role to admin need to generate jwt token again
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PaymentDto>> findAll(){
        log.info("PaymentDto List, controller; fetch all payments");
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<PaymentDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok(service.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public PaymentDto findById(@PathVariable(name = "paymentId")
                               @NotBlank(message = "Input must not be blank")
                               @Valid final String paymentId){
        log.info("PaymentDto, resource; fetch payment by id");
        return service.findById(Integer.parseInt(String.format(paymentId).strip()));
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<OrderDto> getOrderDto(@PathVariable("orderId") final Integer orderId) {
        return ResponseEntity.ok(paymentServiceImpl.getOrderDto(orderId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<PaymentDto> save(
            @RequestBody
            @NotNull(message = "Input must not be NULL!")
            @Valid final PaymentDto paymentDto) {
        log.info("PaymentDto, resource; save payment");
        return ResponseEntity.ok(service.save(paymentDto));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaymentDto> update(
            @RequestBody
            @NotNull(message = "Input must not be NULL")
            @Valid final PaymentDto paymentDto) {
        log.info("PaymentDto, resource; update payment");
        return ResponseEntity.ok(service.update(paymentDto));
    }

    @PutMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<PaymentDto> update(
            @PathVariable("paymentId")
            @NotBlank(message = "Input must not be blank")
            @Valid final String paymentId,
            @RequestBody
            @NotNull(message = "Input must not be NULL")
            @Valid final PaymentDto paymentDto) {
        log.info("PaymentDto, resource; update payment with paymentId");
        return ResponseEntity.ok(service.update(Integer.parseInt(String.format(paymentId).strip()), paymentDto));
    }

    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Boolean> deleteById(@PathVariable("paymentId") final Integer paymentId) {
        log.info("Boolean, resource; delete payment by id");
        service.deleteById(paymentId);
        return ResponseEntity.ok(true);
    }


}
