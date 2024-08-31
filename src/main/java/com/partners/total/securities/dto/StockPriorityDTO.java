package com.partners.total.securities.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPriorityDTO {

    @Schema(description = "계좌 번호", example = "183-9003-57819")
    private String accountNumber;

    @Schema(description = "증권 수량", example = "1")
    private int quantity;

    @Schema(description = "주식 코드", example = "005930")
    private String stockCode;
}
