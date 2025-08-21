package com.heyyoung.solsol.common.entity;

import lombok.Getter;

import java.time.Instant;

@Getter
public class BaseSoftDeleteCreatedEntity extends BaseUpdatedCreatedEntity {
    private Instant deletedAt;

    public void markDeleted() { this.deletedAt = Instant.now(); }
}
