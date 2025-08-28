package com.heyyoung.solsol.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA 및 QueryDSL 설정
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.heyyoung.solsol.domain", 
    repositoryImplementationPostfix = "Impl"  // QueryDSL 구현체 접미사
)
public class JpaConfig {
    // QueryDSL Repository가 자동으로 스캔되고 등록됩니다
}
