package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StocksDTO {

    private int id;
    private int quantity;
    private int accountId;
    private String stockCode;
    private int userId;
    private String companyCode;
}
