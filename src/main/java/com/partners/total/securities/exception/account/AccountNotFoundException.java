package com.partners.total.securities.exception.account;

import java.util.NoSuchElementException;

public class AccountNotFoundException extends NoSuchElementException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
