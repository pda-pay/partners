package com.partners.total.securities.utils;

import com.partners.total.securities.config.WebClientConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockOAuth {

    private final WebClient webClient;
    private final String appkey;
    private final String appsecret;

    public StockOAuth(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.webClient();
        this.appkey = webClientConfig.getAppkey();
        this.appsecret = webClientConfig.getAppsecret();
    }

    public synchronized String fetchOAuthToken() {

        String accessToken;

        try {
            OAuthResponse response = webClient.post()
                    .uri("/oauth2/tokenP")
                    .bodyValue(new OAuthRequestBody("client_credentials", appkey, appsecret))
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        String errorMessage = String.format("Error: %s, Status Code: %d",
                                                errorBody, clientResponse.statusCode().value());
                                        return Mono.error(new RuntimeException(errorMessage));
                                    })
                    )
                    .bodyToMono(OAuthResponse.class)
                    .block();

            accessToken = response.getAccessToken(); // 토큰을 전역 변수에 저장

            System.out.println("accessToken = " + accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            accessToken = null;
        }

        return accessToken;
    }

    public synchronized String getAccessToken(String accessToken) {
        return accessToken != null ? accessToken : fetchOAuthToken(); // 토큰이 없으면 가져옴
    }

    // 내부 클래스들 정의
    @Getter
    private static class OAuthRequestBody {
        private final String grant_type;
        private final String appkey;
        private final String appsecret;

        public OAuthRequestBody(String grant_type, String appkey, String appsecret) {
            this.grant_type = grant_type;
            this.appkey = appkey;
            this.appsecret = appsecret;
        }
    }

    @Setter
    public static class OAuthResponse {
        private String access_token;
        private String access_token_token_expired;
        private String token_type;
        private int expires_in;

        public String getAccessToken() {
            return access_token;
        }

        public String getTokenType() {
            return token_type;
        }

        public int getExpiresIn() {
            return expires_in;
        }
    }
}
