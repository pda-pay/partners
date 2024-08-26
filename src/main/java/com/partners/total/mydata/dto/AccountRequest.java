package com.partners.total.mydata.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountRequest {
    private final String name;
    private final String phoneNumber;
}
