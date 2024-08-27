package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountWithdrawResponse {
    @Schema(description = "계좌번호", example = "125-0369-73009")
    private final String accountNumber;
    @Schema(description = "계좌 잔액", example = "100000")
    private final int deposit;
}
