package com.heyyoung.solsol.domain.settlement.dto;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record FeeStatusResponse(
        int totalStudents,
        int paidCount,
        List<StudentStatus> students
) {
    public static FeeStatusResponse empty() {
        return new FeeStatusResponse(0, 0, Collections.emptyList());
    }

    public record StudentStatus(
            String userId,
            String name,
            String departmentName,
            String studentNumber,
            boolean paid,
            Instant paidAt
    ) {}
}
