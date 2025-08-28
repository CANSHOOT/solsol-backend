package com.heyyoung.solsol.domain.discount.repository;

import com.heyyoung.solsol.domain.discount.CouponStatus;
import com.heyyoung.solsol.domain.discount.CouponType;
import com.heyyoung.solsol.domain.discount.DiscountCouponEntity;
import com.heyyoung.solsol.domain.discount.QDiscountCouponEntity;
import com.heyyoung.solsol.domain.user.entity.QUser;
import com.heyyoung.solsol.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class DiscountCouponQueryRepositoryImpl extends QuerydslRepositorySupport implements DiscountCouponQueryRepository {

    private static final int PAGE = 10_000;      // 키셋 페이지 크기
    private static final int BATCH_FLUSH = 1000; // JPA flush/clear 주기

    @PersistenceContext
    private EntityManager em;

    public DiscountCouponQueryRepositoryImpl() {
        super(DiscountCouponQueryRepositoryImpl.class);
    }

    @Override
    public int issueCouponBucket(LocalDate today, Instant now,
                                 double minInclusive, Double maxInclusiveOrNull,
                                 BigDecimal amount) {
        QUser u = QUser.user;                              // Q타입 이름 확인!
        QDiscountCouponEntity dc = QDiscountCouponEntity.discountCouponEntity;

        int inserted = 0;
        String lastUserId = null; // 문자열 키셋 시작점

        while (true) {
            // 출석률 구간 조건 (max가 null이면 상한 없음)
            BooleanExpression range = (maxInclusiveOrNull == null)
                    ? u.attendanceRate.goe(minInclusive)
                    : u.attendanceRate.between(minInclusive, maxInclusiveOrNull);

            // 오늘 이미 발급된 사용자 제외: DATE(created_at) = today

            BooleanExpression notIssuedToday = JPAExpressions
                    .selectOne()
                    .from(dc)
                    .where(
                            dc.user.userId.eq(u.userId),
                            Expressions.dateTemplate(LocalDate.class, "DATE({0})", dc.createdAt).eq(today)
                            // 만약 createdAt이 부모 Q타입(_super)에 있다면 위 줄을
                            // Expressions.dateTemplate(LocalDate.class, "DATE({0})", dc._super.createdAt).eq(today)
                            // 로 변경 (생성된 Q파일 열어 확인)
                    )
                    .notExists();

            BooleanExpression keyset = (lastUserId == null || lastUserId.isEmpty())
                    ? null
                    : u.userId.gt(lastUserId);

            // 자격 userId 페이지 조회 (키셋)
            List<String> userIds = from(u)
                    .where(
                            u.deletedAt.isNull(),     // 탈퇴 안 한 사용자만
                            range,
                            notIssuedToday,
                            keyset
                    )
                    .orderBy(u.userId.asc())
                    .limit(PAGE)
                    .select(u.userId)
                    .fetch();

            if (userIds.isEmpty()) break;
            lastUserId = userIds.get(userIds.size() - 1);

            // 배치 INSERT (엔티티 직접 persist → flush/clear 주기적으로)
            int i = 0;
            for (String uid : userIds) {
                DiscountCouponEntity entity = DiscountCouponEntity.builder()
                        .user(em.getReference(User.class, uid))  // 프록시 참조로 N+1 회피
                        .couponStatus(CouponStatus.AVAILABLE)
                        .amount(amount)
                        .build();
                entity.setCouponType(CouponType.ATTENDANCE_RATE);

                // createdAt/updatedAt은 Auditing으로 자동 세팅되지만,
                // "당일 1회"를 안전히 하려면 createdAt을 now로 고정시키는 것도 방법임(필요 시 Base에 setter 제공)
                em.persist(entity);
                inserted++;
                i++;

                if (i % BATCH_FLUSH == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.flush();
            em.clear();
        }
        return inserted;
    }
}
