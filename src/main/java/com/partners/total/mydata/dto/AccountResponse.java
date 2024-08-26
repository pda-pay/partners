package com.partners.total.mydata.dto;

import com.partners.total.mydata.domain.Account;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountResponse {
    private final List<Account> accounts;
    public AccountResponse(List<Account> accounts) {
        this.accounts = accounts;
    }
}
