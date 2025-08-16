package org.example.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/*
{"amount":19.99,"currency":"usd","paymentMethodId":"pm_card_visa","paymentType":"gateway","subscriptionPlan":"PRO"}
 */
@Entity
@Table(name = "payment")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String paymentMethodId;
    private String paymentType;
    private String subscriptionPlan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}