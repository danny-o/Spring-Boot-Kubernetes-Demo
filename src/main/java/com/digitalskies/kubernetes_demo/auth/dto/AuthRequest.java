package com.digitalskies.kubernetes_demo.auth.dto;

import jakarta.validation.constraints.*;

public record AuthRequest(
       @Email(message = "Enter a valid email")
       @NotNull(message = "Email is required")
       @NotBlank(message = "Email is required")
       String email,
       @NotNull
       @Size(min=5,message="Enter at least 5 characters")
       String password

) {
}
