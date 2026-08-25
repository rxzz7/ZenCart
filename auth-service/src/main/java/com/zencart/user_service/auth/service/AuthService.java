package com.zencart.user_service.auth.service;

import com.zencart.user_service.auth.activation.ActivateRequest;
import com.zencart.user_service.auth.dto.MessageResponse;
import com.zencart.user_service.auth.login.LoginRequest;
import com.zencart.user_service.auth.passwordreset.ForgotPasswordRequest;
import com.zencart.user_service.auth.passwordreset.ResetPasswordRequest;
import com.zencart.user_service.auth.registration.RegisterRequest;
import com.zencart.user_service.auth.token.RefreshTokenRequest;
import com.zencart.user_service.auth.token.TokenResponse;

public interface AuthService {

    MessageResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse refresh(RefreshTokenRequest request);
    MessageResponse activate(ActivateRequest request);
    MessageResponse forgotPassword(ForgotPasswordRequest request);
    MessageResponse resetPassword(ResetPasswordRequest request);
}
