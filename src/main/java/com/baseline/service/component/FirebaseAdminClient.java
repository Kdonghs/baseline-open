package com.baseline.service.component;

import com.baseline.common.errorcode.MemberErrorCode;
import com.baseline.common.exception.BadRequestException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
public class FirebaseAdminClient {
    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @PostConstruct
    private void init() throws IOException {
        FileInputStream inputStream = new FileInputStream(credentialsPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp.initializeApp(options);
    }

    public FirebaseToken getFirebaseUser(String idToken) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch(Exception e) {
            log.error("invalid id token : {}", idToken, e);
            throw new BadRequestException(MemberErrorCode.INVALID_ID_TOKEN);
        }
    }
}