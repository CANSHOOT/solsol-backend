# 🏃‍♂️ Solsol Backend - 출석률 기반 쿠폰 발급 시스템

## 📋 프로젝트 개요

Solsol Backend는 학생들의 출석률을 기반으로 자동으로 쿠폰을 발급하는 스케줄러 시스템입니다. 매일 새벽 3시 10분에 출석률에 따라 차등적으로 쿠폰을 발급하며, 대용량 배치 처리를 통해 효율적인 데이터 처리를 제공합니다.

### 🎯 주요 기능
- **자동 쿠폰 발급**: 매일 03:10 KST 자동 실행, 출석률에 따른 차등 지급
- **출석률 기반 차등 지급**: 91~100% (500원), 80~90% (100원), 70~79% (50원)
- **대용량 배치 처리**: 10,000건씩 페이징, 1,000건마다 메모리 최적화
- **중복 발급 방지**: 당일 이미 발급된 사용자 제외, 키셋 페이징으로 효율적 처리
- **멀티 프로필 지원**: 개발/테스트/운영 환경별 설정 분리
- **보안 강화**: Jasypt를 통한 민감 정보 암호화, JWT 기반 인증

## 🛠️ 개발 환경

### 필수 요구사항
- **Java**: 21 (OpenJDK 21 또는 Oracle JDK 21)
- **Gradle**: 8.0+ (Wrapper 포함)
- **Database**: MySQL 8.0+
- **OS**: Windows 10+, macOS 10.15+, Ubuntu 18.04+

### 권장 개발 도구
- **IDE**: IntelliJ IDEA 2023.1+, Eclipse 2023-03+, VS Code
- **Database Client**: MySQL Workbench, DBeaver, DataGrip
- **API Testing**: Postman, Insomnia, curl

### 환경 변수
```bash
# Jasypt 암호화 키 (필수)
JASYPT_ENCRYPTOR_PASSWORD=your_encryption_password

# Google Firebase (FCM 알림용, 선택)
GOOGLE_APPLICATION_CREDENTIALS=/path/to/firebase-credentials.json

# 데이터베이스 연결 (프로필별 설정)
SPRING_PROFILES_ACTIVE=profile1  # 개발환경
SPRING_PROFILES_ACTIVE=profile2  # 운영환경
```

## 🚀 빠른 시작

### 1. 프로젝트 클론
```bash
git clone https://github.com/your-username/solsol-backend.git
cd solsol-backend
```

### 2. 환경 설정
```bash
# Jasypt 암호화 키 설정
export JASYPT_ENCRYPTOR_PASSWORD=your_encryption_password

# 또는 Windows PowerShell
$env:JASYPT_ENCRYPTOR_PASSWORD="your_encryption_password"

# 데이터베이스 연결 정보 (필요시)
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=solsol_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### 3. 데이터베이스 설정
```yaml
# application.yml에서 프로필별 데이터베이스 설정 확인
spring:
  profiles:
    group:
      dev: profile1    # 개발환경 (H2 또는 MySQL)
      prod: profile2   # 운영환경 (MySQL)
```

### 4. 애플리케이션 실행
```bash
# 개발환경으로 실행 (H2 데이터베이스 자동 생성)
./gradlew bootRun --args='--spring.profiles.active=profile1'

# 또는 빌드 후 실행
./gradlew build
java -jar build/libs/solsol-0.0.1-SNAPSHOT.jar --spring.profiles.active=profile1

```

## 🔧 빌드 및 실행

### Gradle Wrapper 사용
```bash
# Windows
gradlew.bat clean build
gradlew.bat bootRun

# macOS/Linux
./gradlew clean build
./gradlew bootRun
```

### 프로필별 실행
```bash
# 개발환경
./gradlew bootRun --args='--spring.profiles.active=profile1'

# 운영환경
./gradlew bootRun --args='--spring.profiles.active=profile2'

# 테스트용 (빠른 스케줄링)
./gradlew bootRun --args='--spring.profiles.active=test'
```


## 🧪 테스트

### REST API 테스트
```bash
# 출석률 쿠폰 발급 서비스 테스트
curl -X POST http://localhost:8080/api/test/scheduler/attendance-coupon-service

# 스케줄러 Job 테스트
curl -X POST http://localhost:8080/api/test/scheduler/daily-job

# 응답 예시
# {
#   "success": true,
#   "issuedCount": 35,
#   "executionTimeMs": 1250,
#   "executedAt": "2024-01-15T14:30:00",
#   "message": "출석률 쿠폰 발급 서비스 테스트 완료"
# }
```

## 📊 모니터링 및 로그

### 로그 확인

```bash
# 애플리케이션 로그
tail -f logs/application.log

```

## 🔍 문제 해결

### 일반적인 문제들

#### 1. QueryDSL Q클래스 생성 실패
```bash
# Q클래스 재생성
./gradlew clean compileJava

# 또는
rm -rf build/generated
./gradlew compileJava

# build.gradle에서 QueryDSL 설정 확인
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
```

#### 2. 스케줄러가 실행되지 않음
```java
// SolsolApplication.java에 @EnableScheduling 확인
@SpringBootApplication
@EnableScheduling  // 이 어노테이션이 있어야 함
public class SolsolApplication { ... }

// 또는 application.yml에서 스케줄러 비활성화 확인
scheduler:
  enabled: true
```

#### 3. Jasypt 암호화 오류
```bash
# 환경변수 설정 확인
echo $JASYPT_ENCRYPTOR_PASSWORD

# 또는 application.yml에서 직접 설정
jasypt:
  encryptor:
    password: your_password

# Windows에서 환경변수 설정
set JASYPT_ENCRYPTOR_PASSWORD=your_password
```

#### 5. 메모리 부족 오류
```bash
# JVM 힙 메모리 설정
export JAVA_OPTS="-Xmx2g -Xms1g"

# 또는 실행 시 메모리 설정
java -Xmx2g -Xms1g -jar build/libs/solsol-0.0.1-SNAPSHOT.jar
```


## 📁 프로젝트 구조

```
solsol-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/heyyoung/solsol/
│   │   │       ├── common/           # 공통 설정 및 유틸리티
│   │   │       ├── domain/           # 도메인별 비즈니스 로직
│   │   │       │   ├── schedule/     # 스케줄러 관련
│   │   │       │   ├── discount/     # 쿠폰 관련
│   │   │       │   └── user/         # 사용자 관련
│   │   │       └── external/         # 외부 API 연동
│   │   └── resources/
│   │       ├── application.yml       # 메인 설정 파일
│   │       └── static/               # 정적 리소스
│   └── test/                         # 테스트 코드
├── build.gradle                      # Gradle 빌드 설정
├── gradlew                           # Gradle Wrapper (Unix)
├── gradlew.bat                       # Gradle Wrapper (Windows)
└── README.md                         # 프로젝트 문서
```

## 🔐 보안 설정

### Jasypt 암호화
```yaml
# 민감한 정보 암호화
spring:
  datasource:
    url: ENC(encrypted_database_url)
    username: ENC(encrypted_username)
    password: ENC(encrypted_password)

jwt:
  secret-key: ENC(encrypted_jwt_secret)
```



### 환경별 설정
```bash
# 개발환경
export SPRING_PROFILES_ACTIVE=profile1
export JASYPT_ENCRYPTOR_PASSWORD=dev_password

# 운영환경
export SPRING_PROFILES_ACTIVE=profile2
export JASYPT_ENCRYPTOR_PASSWORD=prod_password
```

## 📚 API 문서

### 주요 엔드포인트
- `POST /api/test/scheduler/attendance-coupon-service` - 쿠폰 발급 서비스 테스트
- `POST /api/test/scheduler/daily-job` - 스케줄러 Job 테스트

### API 응답 형식
```json
{
  "success": true,
  "issuedCount": 35,
  "executionTimeMs": 1250,
  "executedAt": "2024-01-15T14:30:00",
  "message": "출석률 쿠폰 발급 서비스 테스트 완료"
}
```

### 에러 응답 형식
```json
{
  "success": false,
  "error": "데이터베이스 연결 실패",
  "executedAt": "2024-01-15T14:30:00"
}
```


### API 테스트 예시
```bash
# 성공 케이스
curl -X POST http://localhost:8080/api/test/scheduler/attendance-coupon-service \
  -H "Content-Type: application/json"

# 응답 헤더 확인
curl -X POST http://localhost:8080/api/test/scheduler/attendance-coupon-service \
  -v -H "Content-Type: application/json"
```

## 🤝 기여하기

### 개발 가이드라인
1. **코드 스타일**: Google Java Style Guide 준수
2. **테스트**: 새로운 기능에 대한 단위 테스트 작성 필수
3. **문서화**: 주요 변경사항에 대한 문서 업데이트
4. **커밋 메시지**: Conventional Commits 형식 사용


## 🔄 변경 이력

### v0.0.1-SNAPSHOT (2024-01-15)
- ✅ Spring Boot 3.5.4 기반 프로젝트 초기화
- ✅ QueryDSL 5.0.0 설정 및 Repository 구현
- ✅ 출석률 기반 쿠폰 발급 스케줄러 구현
- ✅ 대용량 배치 처리 최적화 (10,000건 페이징, 1,000건 배치)
- ✅ JPA Auditing 및 스케줄링 설정
- ✅ 테스트용 REST API 및 단위 테스트 구현
- ✅ Jasypt 암호화 및 보안 설정
- ✅ 멀티 프로필 지원 (개발/테스트/운영)
- ✅ 성능 모니터링 및 로깅 시스템
- ✅ 예외 처리 및 복구 메커니즘

---

**🚀 Solsol Backend와 함께 효율적인 쿠폰 발급 시스템을 구축해보세요!**
