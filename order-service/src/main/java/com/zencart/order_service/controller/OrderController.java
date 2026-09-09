package com.zencart.order_service.controller;


import com.zencart.order_service.dto.order.OrderDto;
import com.zencart.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController", description = "Operations related to orders")
@Slf4j
public class OrderController {

    private final OrderService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<OrderDto>> findAll(){
        log.info("OrderDto List, controller; fetch all order");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<OrderDto>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "orderId") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok(service.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<OrderDto> findById(@PathVariable(name = "orderId")
                                             @NotBlank(message = "Input must not be blank")
                                             @Valid final String orderId){
        log.info("CartDto, controller; fetch order by id");
        return ResponseEntity.ok(service.findById(Integer.parseInt(String.format(orderId))));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OrderDto> save(@RequestBody
                                             @NotNull(message = "Input must be null")
                                         @Valid final OrderDto orderDto){
        log.info("CartDto, controller; saving order");
        return ResponseEntity.ok(service.save(orderDto));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OrderDto> update(@RequestBody
                                               @NotNull(message = "Input must be null")
                                               @Valid final OrderDto orderDto,
                                           @PathVariable(name = "orderId")
                                           @NotBlank(message = "Input must not be blank")
                                           @Valid final String orderId){
        log.info("CartDto, controller; updating order");
        return ResponseEntity.ok(service.update(Integer.parseInt(String.format(orderId)), orderDto));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "orderId")
                                                  @NotBlank(message = "Input must not be blank")
                                                  @Valid final String orderId){
        log.info("Void, controller; delete order by id");
        service.deleteById(Integer.parseInt(String.format(orderId)));
        return ResponseEntity.ok(true);
    }

    @GetMapping("/existOrderId")
    public ResponseEntity<Boolean> existsByOrderId(@RequestParam Integer orderId) {
        return ResponseEntity.ok(service.existsByOrderId(orderId));
    }

}
