
package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PaymentRequest;
import org.example.dto.PaymentResponse;
import org.example.service.strategy.IPaymentStrategy;
import org.example.service.strategy.PaymentFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
            if (paymentRequest == null) {
                log.error("Payment request is null");
                return PaymentResponse.failed("Payment request cannot be null");
            }

            if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                log.error("Invalid payment amount: {}", paymentRequest.getAmount());
                return PaymentResponse.failed("Payment amount must be greater than zero");
            }

            IPaymentStrategy paymentStrategy = PaymentFactory.getInstance(paymentRequest.getPaymentType());

            if (paymentStrategy == null) {
                log.error("Unsupported payment type: {}", paymentRequest.getPaymentType());
                return PaymentResponse.failed("Unsupported payment type: " + paymentRequest.getPaymentType());
            }

            log.info("Processing payment of {} {} using {} strategy",
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency(),
                    paymentRequest.getPaymentType());

            PaymentResponse response = paymentStrategy.processPayment(paymentRequest);

            log.info("Payment processing completed. Success: {}, Payment ID: {}",
                    response.isSuccess(), response.getPaymentId());

            return response;

        } catch (Exception e) {
            log.error("Unexpected error during payment processing: {}", e.getMessage(), e);
            return PaymentResponse.failed("Payment processing failed due to unexpected error");
        }
    }


}
