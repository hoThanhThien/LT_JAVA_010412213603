package com.example.laundry.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class Firebase {
  private static final Logger log = LoggerFactory.getLogger(Firebase.class);

  @Value("${firebase.config.path}")
  private String firebaseConfigPath;

  @Bean
  @Conditional(FirebaseConfigPresentCondition.class)
  public FirebaseAuth firebaseAuth() throws IOException {
    if (FirebaseApp.getApps().isEmpty()) {
      InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();

      FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .build();

      FirebaseApp.initializeApp(options);
    }

    return FirebaseAuth.getInstance();
  }

  static class FirebaseConfigPresentCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
      String path = context.getEnvironment().getProperty("firebase.config.path", "firebase-service-account.json");
      boolean exists = new ClassPathResource(path).exists();
      if (!exists) {
        log.warn("Không tìm thấy {} trong classpath; bỏ qua khởi tạo FirebaseAuth (đăng ký OTP Firebase sẽ không khả dụng).", path);
      }
      return exists;
    }
  }
}
