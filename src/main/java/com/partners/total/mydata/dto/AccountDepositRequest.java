package com.partners.total.mydata.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDepositRequest {
    private final int accountId;
    private final int value;
}
