package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class DepositRequest {
    @Schema(description = "계좌번호 고유 ID(계좌번호가 아님)", example = "7")
    private int accountId;
}
