package com.heyyoung.solsol.domain.settlement.dto;

public record CouncilFeeTransferCommand(
        Long feeId,                        // 어떤 학기의 회비인지
        String withdrawalAccountNo,
        String depositTransactionSummary,
        String withdrawalTransactionSummary
) {}