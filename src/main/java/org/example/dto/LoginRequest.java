package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    // userName and email are optional (no @NotBlank)
    private String userName;
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public LoginRequest() {}

    // Getters and setters (can be omitted if using Lombok @Getter/@Setter)
}