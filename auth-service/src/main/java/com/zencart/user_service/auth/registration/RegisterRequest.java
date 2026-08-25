package com.zencart.user_service.auth.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        String phone,
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain 8 to 72 characters")
        String password
) {
}
