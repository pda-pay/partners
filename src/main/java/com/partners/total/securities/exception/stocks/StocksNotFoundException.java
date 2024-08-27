package com.partners.total.securities.exception.stocks;

import java.util.NoSuchElementException;

public class StocksNotFoundException extends NoSuchElementException {

    public StocksNotFoundException(String message) {
        super(message);
    }
}
