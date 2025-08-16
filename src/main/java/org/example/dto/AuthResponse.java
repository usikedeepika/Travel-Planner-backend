package org.example.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}