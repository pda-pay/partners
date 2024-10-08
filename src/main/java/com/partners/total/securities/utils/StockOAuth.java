package com.partners.total.securities.utils;

import com.partners.total.securities.config.WebClientConfig;
import com.partners.total.securities.exception.openapi.OpenAPIAccessTokenException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockOAuth {

    private final WebClient webClient;

    @Value("${app.key}")
    private String appkey;

    @Value("${app.secret}")
    private String appsecret;

    public StockOAuth(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.webClient();
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
                                        return Mono.error(new OpenAPIAccessTokenException(errorMessage, clientResponse.statusCode()));
                                    })
                    )
                    .bodyToMono(OAuthResponse.class)
                    .block();

            accessToken = response.getAccessToken(); // 토큰을 전역 변수에 저장

        } catch (Exception e) {
            throw new OpenAPIAccessTokenException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
