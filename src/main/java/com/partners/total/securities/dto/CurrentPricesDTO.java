package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CurrentPricesDTO {

    private List<CurrentPricesDTO.CurrentPriceDTO> currentPricesDTO;

    public CurrentPricesDTO() {
        this.currentPricesDTO = new ArrayList<>();
    }

    public void addPreviousDTO(String stockCode, String amount) {

        int parsedAmount = Integer.parseInt(amount);

        this.currentPricesDTO.add(new CurrentPricesDTO.CurrentPriceDTO(stockCode, parsedAmount));
    }

    @Getter
    @Setter
    public static class CurrentPriceDTO {

        private String stockCode;
        private int amount;

        public CurrentPriceDTO() {}

        public CurrentPriceDTO(String stockCode, int amount) {
            this.stockCode = stockCode;
            this.amount = amount;
        }
    }
}
