package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class DepositRequest {
    @Schema(description = "계좌번호", example = "125-0369-73009")
    private String accountNumber;
}
