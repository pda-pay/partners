package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPriorityDTO {

    private int accountId;
    private int quantity;
    private String stockCode;
}
