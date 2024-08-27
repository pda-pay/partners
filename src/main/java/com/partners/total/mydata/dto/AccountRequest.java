package com.partners.total.mydata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountRequest {
    @Schema(description = "이름", example = "강경순")
    private final String name;
    @Schema(description = "핸드폰 번호", example = "010-7777-2929")
    private final String phoneNumber;
}
