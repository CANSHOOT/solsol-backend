package com.heyyoung.solsol.domain.merchant;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.menu.MenuEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "merchants")
@ToString
public class MerchantEntity extends BaseUpdatedCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long merchantId;

    private String merchantName;
    @Enumerated(EnumType.STRING)
    private MerchantType merchantType;

    private String location;
    private BigDecimal baseDiscountRate;
    private boolean supportsQr;
    private boolean supportsNfc;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MenuEntity> menus;

    @Builder
    public MerchantEntity(String merchantName, MerchantType merchantType, String location, BigDecimal baseDiscountRate,
                          boolean supportsQr, boolean supportsNfc) {
        this.merchantName = merchantName;
        this.merchantType = merchantType;
        this.location = location;
        this.baseDiscountRate = baseDiscountRate;
        this.supportsQr = supportsQr;
        this.supportsNfc = supportsNfc;
    }
}
