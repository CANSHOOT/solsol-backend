package com.heyyoung.solsol.external.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TransactionHistoryRecord {
    
    private String totalCount;
    private List<TransactionHistoryDetail> list;
}