package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDepositRequest {
    @Schema(description = "계좌번호 고유 ID(계좌번호가 아님)", example = "7")
    private final int accountId;
    @Schema(description = "증감할 금액", example = "100000")
    private final int value;
}
