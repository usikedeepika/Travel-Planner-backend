package org.example.service.strategy;

import org.example.dto.PaymentRequest;
import org.example.dto.PaymentResponse;

public interface IPaymentStrategy {

    PaymentResponse processPayment(PaymentRequest paymentRequest);
}
