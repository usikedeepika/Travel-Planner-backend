package org.example.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TokenController {
    private final UserService userService;
    Logger logger = Logger.getLogger(String.valueOf(TokenController.class));

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<AuthResponse>> generateToken(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = userService.Login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Token generated successfully", authResponse));
        } catch (Exception e) {
            logger.log(Level.FINE, "Error while generating token: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}