package com.partners.total.mydata.exception;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends RuntimeException {
    private final int accountId;
    private final int deposit;
    public InsufficientBalanceException(String message, int accountId, int deposit) {
        super(message);
        this.accountId = accountId;
        this.deposit = deposit;
    }
}
