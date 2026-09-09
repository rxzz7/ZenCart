package com.zencart.order_service.controller;


import com.zencart.order_service.dto.order.CartDto;
import com.zencart.order_service.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
@Tag(name = "CartController", description = "Operations related to carts")
@Slf4j
public class CartController {

    private final CartService service;


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<CartDto>> findAll(){
        log.info("CartDto List controller; fetching all carts");
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<CartDto>> findAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "cartId") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok(service.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{cartId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CartDto> findById(@PathVariable(name = "cartId")
                                                @NotBlank(message = "Input must not be null")
                                            @Valid final String cartId){
        log.info("CartDto, controller; fetch cart by id");
        return ResponseEntity.ok(service.findById(Integer.parseInt(String.format(cartId).strip())));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CartDto> save(@RequestBody
                                            @NotNull(message = "Input must not be null")
                                        @Valid final CartDto cartDto){
        log.info("CartDto, controller; saving cart ");
        return ResponseEntity.ok(service.save(cartDto));

    }

    @PutMapping("/{cartId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CartDto> update(@RequestBody
                                              @NotNull(message = "Input must not be null")
                                              @Valid final CartDto cartDto,
                                          @PathVariable(name = "cartId")
                                          @NotBlank(message = "Input must not be blank")
                                          @Valid final String cartId){
        log.info("CartDto, controller; updating cart");
        return ResponseEntity.ok(service.update(Integer.parseInt(String.format(cartId).strip()), cartDto));
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "cartId")
                                                  @NotBlank(message = "Input must not be blank")
                                                  @Valid final String cartId){
        log.info("Void, controller; delete by cart id");
        service.deleteById(Integer.parseInt(String.format(cartId).strip()));
        return ResponseEntity.ok(true);
    }


}
