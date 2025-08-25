package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.account.entity.Account;
import com.heyyoung.solsol.domain.merchant.MerchantEntity;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "payments")
public class PaymentsEntity extends BaseUpdatedCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private MerchantEntity merchant;

    private BigDecimal originalAmount;
    private BigDecimal discountAmount;

    private BigDecimal discountRate;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionSummary;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String apiTransactionId;
}
