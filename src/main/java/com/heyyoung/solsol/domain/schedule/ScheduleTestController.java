package com.heyyoung.solsol.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 스케줄러 테스트용 컨트롤러
 * 개발/테스트 환경에서만 사용
 */
@RestController
@RequestMapping("/test/scheduler")
@RequiredArgsConstructor
@Slf4j
public class ScheduleTestController {

    private final AttendanceTieredCouponService attendanceTieredCouponService;
    private final DailyAttendanceCouponJob dailyAttendanceCouponJob;

    /**
     * 수동으로 출석률 쿠폰 발급 서비스 테스트
     */
    @PostMapping("/attendance-coupon-service")
    public Map<String, Object> testAttendanceCouponService() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            int issuedCount = attendanceTieredCouponService.issueDailyCouponsTiered();
            long endTime = System.currentTimeMillis();
            
            result.put("success", true);
            result.put("issuedCount", issuedCount);
            result.put("executionTimeMs", endTime - startTime);
            result.put("executedAt", LocalDateTime.now());
            result.put("message", "출석률 쿠폰 발급 서비스 테스트 완료");
            
            log.info("테스트 실행 완료: {}건 발급, {}ms 소요", issuedCount, endTime - startTime);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("executedAt", LocalDateTime.now());
            log.error("테스트 실행 중 오류 발생", e);
        }
        
        return result;
    }

    /**
     * 수동으로 스케줄러 Job 테스트
     */
    @PostMapping("/daily-job")
    public Map<String, Object> testDailyJob() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            dailyAttendanceCouponJob.run();
            long endTime = System.currentTimeMillis();
            
            result.put("success", true);
            result.put("executionTimeMs", endTime - startTime);
            result.put("executedAt", LocalDateTime.now());
            result.put("message", "스케줄러 Job 테스트 완료");
            
            log.info("스케줄러 Job 테스트 완료: {}ms 소요", endTime - startTime);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("executedAt", LocalDateTime.now());
            log.error("스케줄러 Job 테스트 중 오류 발생", e);
        }
        
        return result;
    }
}
