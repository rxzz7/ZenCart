package com.zencart.user_service.auth.activation;

import jakarta.validation.constraints.NotBlank;

public record ActivateRequest(
        @NotBlank(message = "Activation token is required")
        String token
) {
}
