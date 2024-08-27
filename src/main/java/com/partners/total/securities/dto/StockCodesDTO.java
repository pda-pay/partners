package com.partners.total.securities.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class StockCodesDTO {

    @Schema(description = "Stock codes", example = "[\"005930\", \"000020\", \"000040\"]")
    private List<String> stockCodes;
}
