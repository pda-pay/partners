package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountForRepaymentResponse {
    @Schema(description = "계좌 잔액", example = "700000")
    private final int deposit;

    @Schema(description = "회사 코드", example = "004")
    private final String companyCode;
}
