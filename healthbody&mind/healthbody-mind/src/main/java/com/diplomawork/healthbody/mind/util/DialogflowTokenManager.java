package com.diplomawork.healthbody.mind.util;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Component
public class DialogflowTokenManager {

    private static final String DIALOGFLOW_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String KEY_FILE_PATH = "src/main/resources/healthbody-mind-a54aef5f5ad5.json";

    private AccessToken cachedToken;

    public String getAccessToken() throws IOException {
        if (cachedToken == null || cachedToken.getExpirationTime().before(new java.util.Date())) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(KEY_FILE_PATH))
                    .createScoped(Collections.singleton(DIALOGFLOW_SCOPE));
            credentials.refreshIfExpired();
            cachedToken = credentials.getAccessToken();
        }

        return cachedToken.getTokenValue();
    }
}