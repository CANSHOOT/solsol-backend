package com.heyyoung.solsol.external.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                var path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
                if (path == null || path.trim().isEmpty()) {
                    throw new RuntimeException("GOOGLE_APPLICATION_CREDENTIALS 환경변수가 설정되지 않았습니다.");
                }

                try (var in = new FileInputStream(path)) {
                    var options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(in))
                            .build();
                    FirebaseApp.initializeApp(options);
                    log.info("Firebase 초기화 성공 - 경로: {}", path);
                }
            }
        } catch (Exception e) {
            log.error("Firebase 초기화 실패", e);
        }
    }
}