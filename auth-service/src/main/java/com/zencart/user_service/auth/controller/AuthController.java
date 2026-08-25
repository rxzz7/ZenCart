package com.zencart.user_service.auth.controller;

import com.zencart.user_service.auth.activation.ActivateRequest;
import com.zencart.user_service.auth.dto.MessageResponse;
import com.zencart.user_service.auth.login.LoginRequest;
import com.zencart.user_service.auth.passwordreset.ForgotPasswordRequest;
import com.zencart.user_service.auth.passwordreset.ResetPasswordRequest;
import com.zencart.user_service.auth.registration.RegisterRequest;
import com.zencart.user_service.auth.service.AuthService;
import com.zencart.user_service.auth.token.RefreshTokenRequest;
import com.zencart.user_service.auth.token.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register (@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/activate")
    public ResponseEntity<MessageResponse> activate(@Valid @RequestBody ActivateRequest request) {
        return ResponseEntity.ok(authService.activate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.accepted().body(authService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
