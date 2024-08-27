package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PreviousPricesDTO {

    private List<PreviousPriceDTO> previousPricesDTO;

    public PreviousPricesDTO() {
        this.previousPricesDTO = new ArrayList<>();
    }

    public void addPreviousDTO(String stockCode, String amount) {

        int parsedAmount = Integer.parseInt(amount);

        this.previousPricesDTO.add(new PreviousPriceDTO(stockCode, parsedAmount));
    }

    @Getter
    @Setter
    public static class PreviousPriceDTO {

        private String stockCode;
        private int amount;

        public PreviousPriceDTO() {}

        public PreviousPriceDTO(String stockCode, int amount) {
            this.stockCode = stockCode;
            this.amount = amount;
        }
    }
}
