package com.heyyoung.solsol.domain.settlement.service;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.settlement.entity.CouncilFee;
import com.heyyoung.solsol.domain.settlement.entity.CouncilFeePayment;
import com.heyyoung.solsol.domain.settlement.entity.FeePaymentStatus;
import com.heyyoung.solsol.domain.settlement.repository.CouncilFeePaymentRepository;
import com.heyyoung.solsol.domain.settlement.repository.CouncilFeeRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import com.heyyoung.solsol.external.dto.account.AccountBalanceResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CouncilFeeTransferService {
    private final UserRepository userRepository;
    private final AccountApiService accountApiService;
    private final CouncilFeePaymentRepository councilFeePaymentRepository;
    private final CouncilFeeRepository councilFeeRepository;

    public String getUserKeyById(String userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));
        if(user != null) {
            return user.getUserKey();
        }

        throw new SolsolException(SolsolErrorCode.USER_NOT_FOUND);
    }

    public String getAccountNoByUser(String userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));
        if(user != null) {
            return user.getAccountNo();
        }

        throw new SolsolException(SolsolErrorCode.USER_NOT_FOUND);
    }


    /**
     * 사용자 잔액 업데이트
     * 금융 API를 통해 최신 잔액을 조회하여 DB에 반영
     * @param userId 업데이트할 사용자Id
     */
    public void updateBalance(String userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));

        try {
            AccountBalanceResponse balanceResponse = accountApiService.inquireAccountBalance(
                    user.getUserKey(),
                    user.getAccountNo()
            );

            // 잔액을 Long 타입으로 변환 (String -> Long)
            Long newBalance = Long.parseLong(balanceResponse.getREC().getAccountBalance());
            user.updateAccountBalance(newBalance);

            log.info("사용자 잔액 업데이트 완료 - UserId: {}, AccountNo: {}, Balance: {}",
                    user.getUserId(), user.getAccountNo(), newBalance);

        } catch (Exception e) {
            log.error("사용자 잔액 업데이트 실패 - UserId: {}, Error: {}",
                    user.getUserId(), e.getMessage());
            // 잔액 업데이트 실패해도 이체는 완료되었으므로 예외를 던지지 않음
        }
    }


    @Transactional
    public void recordPayment(Long feeId, String userId, BigDecimal amount) {
        CouncilFeePayment payment = CouncilFeePayment.builder()
                .feeId(feeId)
                .userId(userId)
                .amount(amount)
                .transactionId("TXN001")
                .paymentStatus(FeePaymentStatus.COMPLETED)
                .paidAt(Instant.now())
                .build();

        councilFeePaymentRepository.save(payment);
    }

    public BigDecimal getFeeAmountById(Long feeId) {
        CouncilFee fee = councilFeeRepository.findById(feeId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.PAYMENT_FAILED));

        return fee.getFeeAmount();
    }

    public List<CouncilFee> getAllFees() {
        return councilFeeRepository.findAll();
    }
}
