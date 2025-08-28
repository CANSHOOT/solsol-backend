package com.heyyoung.solsol.domain.schedule;

import com.heyyoung.solsol.domain.schedule.AttendanceTieredCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyAttendanceCouponJob {

    private final AttendanceTieredCouponService service;

    // 매일 03:10 KST
    @Scheduled(cron = "0 10 3 * * *", zone = "Asia/Seoul")
    @Transactional
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            int n = service.issueDailyCouponsTiered();
            long endTime = System.currentTimeMillis();
            log.info("DailyAttendanceCouponJob 성공: {}건 발급, {}ms 소요", n, endTime - startTime);
        } catch (Exception e) {
            log.error("DailyAttendanceCouponJob 실행 중 오류 발생", e);
            // TODO: 알림 서비스 호출 또는 모니터링 시스템에 알림
        }
    }
}
