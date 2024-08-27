package com.partners.total.securities.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AllStocksRequestDTO {

    private int id;
    private int quantity;
    private String stockCode;

    public AllStocksRequestDTO() {}

    public AllStocksRequestDTO(int id, int quantity, String stockCode) {
        this.id = id;
        this.quantity = quantity;
        this.stockCode = stockCode;
    }
}
