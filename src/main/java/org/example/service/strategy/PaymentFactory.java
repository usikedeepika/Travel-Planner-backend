package org.example.service.strategy;
//
//public class PaymentFactory {
//    public static IPaymentStrategy getInstance(String type)
//    {
//        return switch (type) {
//            case "gateway" -> new ApiGateWayPayments();
//            case "crypto" -> new CryptoPayments();
//            default -> null;
//        };
//    }
//}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    private static  ApiGateWayPayments apiGateWayPayments;
    private static  CryptoPayments cryptoPayments;

    @Autowired
    public PaymentFactory(ApiGateWayPayments apiGateWayPayments, CryptoPayments cryptoPayments) {
        this.apiGateWayPayments = apiGateWayPayments;
        this.cryptoPayments = cryptoPayments;
    }

    public static IPaymentStrategy getInstance(String type) {
        return switch (type) {
            case "gateway" -> apiGateWayPayments;
            case "crypto" -> cryptoPayments;
            default -> null;
        };
    }
}