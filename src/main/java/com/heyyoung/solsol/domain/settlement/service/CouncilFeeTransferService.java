package com.heyyoung.solsol.domain.settlement.service;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouncilFeeTransferService {
    private final UserRepository userRepository;

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
}
