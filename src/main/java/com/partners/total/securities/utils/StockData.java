package com.partners.total.securities.utils;

import com.partners.total.securities.dto.ClosePriceDTO;
import com.partners.total.securities.dto.CurrentPriceDTO;
import com.partners.total.securities.config.WebClientConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class StockData {

    private final WebClient webClient;
    private final String appkey;
    private final String appsecret;

    public StockData(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.webClient();
        this.appkey = webClientConfig.getAppkey();
        this.appsecret = webClientConfig.getAppsecret();
    }

    public synchronized ClosePriceDTO fetchClosePriceData(String code, String accessToken) {

        ClosePriceDTO response = null;

        try {
            response = webClient.get()
                    .uri(uriBuilder ->
                        uriBuilder
                                .path("/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
                                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                                .queryParam("FID_INPUT_ISCD", code)
                                .queryParam("FID_INPUT_DATE_1", getYesterdayDateString())
                                .queryParam("FID_INPUT_DATE_2", getTodayDateString())
                                .queryParam("FID_PERIOD_DIV_CODE", "D")
                                .queryParam("FID_ORG_ADJ_PRC", "1")
                                .build()
                    )
                    .headers(headers -> {
                        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                        headers.set("appkey", appkey);
                        headers.set("appsecret", appsecret);
                        headers.set("tr_id", "FHKST03010100");
                    })
                    .retrieve()
                    .bodyToMono(ClosePriceDTO.class)
                    .onErrorResume(e -> {
                        System.err.println("전일 종가 가져오기 에러 = " + e.getMessage());
                        return Mono.empty();
                    })
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getYesterdayDateString(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return yesterday.format(formatter);
    }

    private static String getTodayDateString() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return today.format(formatter);
    }

    public synchronized CurrentPriceDTO fetchCurrentPriceData(String code, String accessToken) {

        CurrentPriceDTO response = null;

        try {
            response = webClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                                    .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                                    .queryParam("FID_INPUT_ISCD", code)
                                    .build()
                    )
                    .headers(headers -> {
                        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                        headers.set("appkey", appkey);
                        headers.set("appsecret", appsecret);
                        headers.set("tr_id", "FHKST01010100");
                    })
                    .retrieve()
                    .bodyToMono(CurrentPriceDTO.class)
                    .onErrorResume(e -> {
                        System.err.println("현재가 가져오기 에러 = " + e.getMessage());
                        return Mono.empty();
                    })
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
