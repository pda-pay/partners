package com.partners.total.mydata.service;

import com.partners.total.mydata.domain.*;
import com.partners.total.mydata.dto.*;
import com.partners.total.mydata.exception.CalculateAccountError;
import com.partners.total.mydata.exception.AccountIdNotFoundException;
import com.partners.total.mydata.exception.InsufficientBalanceException;
import com.partners.total.mydata.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MydataService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StocksRepository stocksRepository;
    @Transactional
    public MydataResponse getMydata(MydataRequest mydataRequest) {
         User user = userRepository.findUserByNameAndPhoneNumber(mydataRequest.getName(), mydataRequest.getPhoneNumber())
                .orElseGet(() -> {
                    User fakeUser = createFakeUser(mydataRequest.getName(), mydataRequest.getPhoneNumber());
                    List<Account> fakeAccounts = createFakeAccounts(fakeUser);
                    fakeUser.setAccounts(fakeAccounts);
                    List<Stocks> stocksList = createFakeStocks(fakeAccounts.get(1));
                    fakeAccounts.get(0).setStocksList(new ArrayList<>());
                    fakeAccounts.get(1).setStocksList(stocksList);
                    return fakeUser;
                });

        return new MydataResponse(user);
    }

    @Transactional
    public AccountResponse getAccounts(AccountRequest accountRequest) {
        User user = userRepository.findUserByNameAndPhoneNumber(accountRequest.getName(), accountRequest.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾지 못했습니다."));
        List<Account> accounts = accountRepository.findByUser(user);

        return new AccountResponse(accounts);
    }

    @Transactional
    public DepositResponse getDepositByAccountId(DepositRequest depositRequest) {
        final int deposit = accountRepository.findDepositById(depositRequest.getAccountId())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));

        return new DepositResponse(deposit);
    }

    @Transactional
    public AccountDepositResponse depositAccount(AccountDepositRequest accountDepositRequest) {
        final int deposit = accountRepository.findDepositById(accountDepositRequest.getAccountId())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));
        final int newValue = deposit + accountDepositRequest.getValue();
        updateDepositValue(accountDepositRequest.getAccountId(), newValue);

        return new AccountDepositResponse(accountDepositRequest.getAccountId(), newValue);
    }

    @Transactional
    public AccountWithdrawResponse withdrawAccount(AccountWithdrawRequest accountWithdrawRequest) {
        final int deposit = accountRepository.findDepositById(accountWithdrawRequest.getAccountId())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));
        final int newValue = deposit - accountWithdrawRequest.getValue();

        if (isDepositAmountValid(newValue)) {
            updateDepositValue(accountWithdrawRequest.getAccountId(), newValue);
        } else {
            throw new InsufficientBalanceException("계좌 잔액이 부족합니다.", accountWithdrawRequest.getAccountId(), deposit);
        }

        return new AccountWithdrawResponse(accountWithdrawRequest.getAccountId(), newValue);
    }

    private boolean isDepositAmountValid(int value) {
        return value >= 0 ? true : false;
    }

    private void updateDepositValue(int accountId, int newValue) {
        accountRepository.updateDepositById(accountId, newValue)
                .orElseThrow(() -> new CalculateAccountError("계좌를 업데이트하는데 오류가 발생했습니다."));
    }

    private User createFakeUser(String name, String phoneNumber) {
        User newUser = new User(name, phoneNumber);
        return userRepository.save(newUser);
    }

    private List<Account> createFakeAccounts(User user) {
        String fakeAccountNumber = "223-223-111999";
        String fakeAccountNumber2 = "111-777-123239";
        int fakeDeposit = 1000000;
        int fakeDeposit2 = 2000000;
        String fakeCompanyCode = "01";
        String fakeCompanyCode2 = "11";
        String fakeCategory = "01";
        String fakeCategory2 = "02";
        List<Account> accounts = new ArrayList<>();

        Account fakeAccount = new Account(fakeAccountNumber, fakeDeposit, user,fakeCompanyCode, fakeCategory);
        Account fakeAccount2 = new Account(fakeAccountNumber2, fakeDeposit2, user,fakeCompanyCode2, fakeCategory2);

        accounts.add(fakeAccount);
        accounts.add(fakeAccount2);

        accountRepository.saveAll(accounts);
        return accounts;
    }

    private List<Stocks> createFakeStocks(Account account) {
        List<Stocks> stocksList = new ArrayList<>();
        Stocks fakeStocks = new Stocks(20, account, "005930");
        Stocks fakeStocks2 = new Stocks(40, account, "373220");

        stocksList.add(fakeStocks);
        stocksList.add(fakeStocks2);

        stocksRepository.saveAll(stocksList);
        return stocksList;
    }
}
