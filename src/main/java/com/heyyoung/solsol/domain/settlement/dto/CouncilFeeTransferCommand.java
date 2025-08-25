package com.heyyoung.solsol.domain.settlement.dto;

/** 회비 송금 요청 (사용자키/입금계좌는 서버에서 주입) */
public record CouncilFeeTransferCommand(

        /** 출금 계좌번호 */
        String withdrawalAccountNo,

        /** 이체 금액(원) - 숫자만 */
        String transactionBalance,

        /** (선택) 출금 거래 요약(미지정 시 기본값 사용) */
        String withdrawalTransactionSummary,

        /** (선택) 입금 거래 요약(미지정 시 기본값 사용) */
        String depositTransactionSummary
) { }
