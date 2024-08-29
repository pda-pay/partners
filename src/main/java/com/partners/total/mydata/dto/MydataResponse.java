package com.partners.total.mydata.dto;

import com.partners.total.mydata.domain.Account;
import com.partners.total.mydata.domain.Stocks;
import com.partners.total.mydata.domain.User;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MydataResponse {
    private final List<AccountResponse> accounts;

    public MydataResponse(User user) {
        this.accounts = user.getAccounts().stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class AccountResponse {
        private final String accountNumber;
        private final int deposit;
        private final String companyCode;
        private final String category;
        private final List<StocksResponse> stocks;

        public AccountResponse(Account account) {
            this.accountNumber = account.getAccountNumber();
            this.deposit = account.getDeposit();
            this.companyCode = account.getCompanyCode();
            this.category = account.getCategory();
            this.stocks = account.getStocksList().stream()
                    .map(StocksResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class StocksResponse {
        private final int quantity;
        private final String stockCode;

        public StocksResponse(Stocks stocks) {
            this.quantity = stocks.getQuantity();
            this.stockCode = stocks.getStockCode();
        }
    }
}