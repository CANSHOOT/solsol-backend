package com.heyyoung.solsol.common.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
public class BaseUpdatedCreatedEntity extends BaseCreatedEntity {
    @Column(nullable = false, updatable = false)
    @LastModifiedDate
    private Instant updatedAt;
}
