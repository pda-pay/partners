package com.partners.total.mydata.exception;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends RuntimeException {
    private final String accountNumber;
    private final int deposit;
    public InsufficientBalanceException(String message, String accountNumber, int deposit) {
        super(message);
        this.accountNumber = accountNumber;
        this.deposit = deposit;
    }
}
