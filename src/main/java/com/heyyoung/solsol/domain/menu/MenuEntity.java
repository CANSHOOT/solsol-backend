package com.heyyoung.solsol.domain.menu;

import com.heyyoung.solsol.common.entity.BaseSoftDeleteCreatedEntity;
import com.heyyoung.solsol.merchant.MerchantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "menu")
public class MenuEntity extends BaseSoftDeleteCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private MerchantEntity merchant;

    private BigDecimal price;
    private String name;
    private String image;
}
