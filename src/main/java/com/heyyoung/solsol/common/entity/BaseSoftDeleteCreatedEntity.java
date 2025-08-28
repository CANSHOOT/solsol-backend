package com.heyyoung.solsol.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseSoftDeleteCreatedEntity extends BaseUpdatedCreatedEntity {
    private Instant deletedAt;

    public void markDeleted() {
        this.deletedAt = Instant.now();
    }
}
