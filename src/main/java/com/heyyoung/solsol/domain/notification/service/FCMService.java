package com.heyyoung.solsol.domain.notification.service;

import com.google.firebase.messaging.*;
import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayParticipant;
import com.heyyoung.solsol.domain.dutchpay.repository.DutchPayParticipantRepository;
import com.heyyoung.solsol.domain.dutchpay.service.DutchPayService;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * FCM 푸시 알림 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final UserRepository userRepository;
    private final DutchPayParticipantRepository  dutchPayParticipantRepository;
    /**
     * 더치페이 정산 요청 푸시 알림 전송
     */
    public void sendDutchPayInviteNotification(String fcmToken, String organizerName, String groupName, Long groupId, String userId) {
        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            log.warn("FCM 토큰이 비어있어 알림 전송을 건너뜁니다.");
            return;
        }

        try {
            DutchPayParticipant dp = dutchPayParticipantRepository.findByDutchPayGroup_GroupIdAndUser_UserId(groupId, userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.PARTICIPANT_NOT_FOUND));

            Map<String, String> data = new HashMap<>();
            data.put("type", "DUTCH_PAY_INVITE");
            data.put("groupId", groupId.toString());
            data.put("groupName", groupName);
            data.put("amount", dp.getSettlementAmount().toString());

            Message message = Message.builder()
                    .setToken(fcmToken)
//                    .setNotification(Notification.builder()
//                            .setTitle("정산 요청이 왔어요!")
//                            .setBody(organizerName + "님이 " + groupName + " 정산을 요청했어요")
//                            .build())
                    .putAllData(data)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    // click_action 제거 (네이티브 앱은 불필요)
                                    .setIcon("ic_notification")
                                    .setColor("#FF6B35")
                                    .build())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("더치페이 초대 푸시 알림 전송 성공 - GroupId: {}, Response: {}", groupId, response);

        } catch (FirebaseMessagingException e) {
            log.warn("FCM 전송 실패: {} / {}", e.getErrorCode(), e.getMessage());

            // 무효 토큰 처리
            if ("UNREGISTERED".equals(e.getErrorCode()) || "INVALID_ARGUMENT".equals(e.getErrorCode())) {
                clearInvalidFcmToken(fcmToken);
            }
        } catch (Exception e) {
            log.error("더치페이 초대 푸시 알림 전송 실패 - GroupId: {}, Error: {}", groupId, e.getMessage());
        }
    }

    /**
     * 더치페이 참여 알림 전송 (주최자에게)
     */
    public void sendDutchPayJoinNotification(String organizerFcmToken, String participantName, String groupName) {
        if (organizerFcmToken == null || organizerFcmToken.trim().isEmpty()) {
            log.warn("주최자 FCM 토큰이 비어있어 참여 알림을 건너뜁니다.");
            return;
        }

        try {
            Map<String, String> data = new HashMap<>();
            data.put("type", "DUTCH_PAY_JOIN");
            data.put("groupName", groupName);

            Message message = Message.builder()
                    .setToken(organizerFcmToken)
                    .setNotification(Notification.builder()
                            .setTitle("정산에 참여했어요!")
                            .setBody(participantName + "님이 " + groupName + " 정산에 참여했어요")
                            .build())
                    .putAllData(data)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setIcon("ic_notification")
                                    .setColor("#4CAF50")
                                    .build())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("더치페이 참여 푸시 알림 전송 성공 - Response: {}", response);

        } catch (FirebaseMessagingException e) {
            log.warn("FCM 전송 실패: {} / {}", e.getErrorCode(), e.getMessage());
            if ("UNREGISTERED".equals(e.getErrorCode()) || "INVALID_ARGUMENT".equals(e.getErrorCode())) {
                clearInvalidFcmToken(organizerFcmToken);
            }
        } catch (Exception e) {
            log.error("더치페이 참여 푸시 알림 전송 실패 - Error: {}", e.getMessage());
        }
    }

    /**
     * 더치페이 송금 완료 알림 전송 (주최자에게)
     */
    public void sendDutchPayPaymentNotification(String organizerFcmToken, String participantName, String groupName, String amount) {
        if (organizerFcmToken == null || organizerFcmToken.trim().isEmpty()) {
            log.warn("주최자 FCM 토큰이 비어있어 송금 완료 알림을 건너뜁니다.");
            return;
        }

        try {
            Map<String, String> data = new HashMap<>();
            data.put("type", "DUTCH_PAY_PAYMENT");
            data.put("groupName", groupName);
            data.put("amount", amount);

            Message message = Message.builder()
                    .setToken(organizerFcmToken)
                    .setNotification(Notification.builder()
                            .setTitle("송금이 완료되었어요!")
                            .setBody(participantName + "님이 " + amount + "원을 송금했어요")
                            .build())
                    .putAllData(data)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setIcon("ic_notification")
                                    .setColor("#2196F3")
                                    .build())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("더치페이 송금 완료 푸시 알림 전송 성공 - Response: {}", response);

        } catch (FirebaseMessagingException e) {
            log.warn("FCM 전송 실패: {} / {}", e.getErrorCode(), e.getMessage());
            if ("UNREGISTERED".equals(e.getErrorCode()) || "INVALID_ARGUMENT".equals(e.getErrorCode())) {
                clearInvalidFcmToken(organizerFcmToken);
            }
        } catch (Exception e) {
            log.error("더치페이 송금 완료 푸시 알림 전송 실패 - Error: {}", e.getMessage());
        }
    }

    /**
     * 무효한 FCM 토큰 정리
     */
    @Transactional
    public void clearInvalidFcmToken(String invalidToken) {
        try {
            userRepository.findByFcmToken(invalidToken)
                    .ifPresent(user -> {
                        user.updateFcmToken(null);
                        userRepository.save(user);
                        log.info("무효 FCM 토큰 정리 완료 - UserId: {}", user.getUserId());
                    });
        } catch (Exception e) {
            log.error("무효 FCM 토큰 정리 실패 - Token: {}, Error: {}", invalidToken, e.getMessage());
        }
    }
}