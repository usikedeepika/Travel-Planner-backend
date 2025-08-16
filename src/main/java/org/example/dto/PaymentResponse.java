package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String paymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String description;
    private LocalDateTime createdAt;
    private String clientSecret; // For Stripe frontend confirmation
    private String receiptUrl;
    private boolean success;
    private String errorMessage;

    // Factory methods for easier creation
    public static PaymentResponse success(String paymentId, BigDecimal amount, String currency, String clientSecret) {
        return PaymentResponse.builder()
                .paymentId(paymentId)
                .status("succeeded")
                .amount(amount)
                .currency(currency)
                .clientSecret(clientSecret)
                .createdAt(LocalDateTime.now())
                .success(true)
                .build();
    }

    public static PaymentResponse failed(String errorMessage) {
        return PaymentResponse.builder()
                .status("failed")
                .errorMessage(errorMessage)
                .createdAt(LocalDateTime.now())
                .success(false)
                .build();
    }
}