package com.example.laundry.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class Firebase {

  @Value("${firebase.config.path}")
  private String firebaseConfigPath;

  @Bean
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
}