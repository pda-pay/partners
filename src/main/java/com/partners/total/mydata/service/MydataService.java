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
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MydataService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StocksRepository stocksRepository;
    private final Random random = new Random();

    @Transactional
    public MydataResponse getMydata(MydataRequest mydataRequest) {
        User user = userRepository.findUserByNameAndPhoneNumber(mydataRequest.getName(), mydataRequest.getPhoneNumber())
                .orElseGet(() -> {
                    User fakeUser = createFakeUser(mydataRequest.getName(), mydataRequest.getPhoneNumber());
                    List<Account> fakeAccounts = createFakeAccounts(fakeUser);
                    fakeUser.setAccounts(fakeAccounts);
                    assignStocksToAccountsBasedOnCategory(fakeAccounts);

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
    public AccountForRepaymentResponse getDepositByAccountNumber(DepositRequest depositRequest) {
        AccountForRepaymentResponse response = accountRepository.findDepositAndCompanyCodeByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));

        return response;
    }

    @Transactional
    public AccountDepositResponse depositAccount(AccountDepositRequest accountDepositRequest) {
        final int deposit = accountRepository.findDepositByAccountNumber(accountDepositRequest.getAccountNumber())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));
        final int newValue = deposit + accountDepositRequest.getValue();
        updateDepositValue(accountDepositRequest.getAccountNumber(), newValue);

        return new AccountDepositResponse(accountDepositRequest.getAccountNumber(), newValue);
    }

    @Transactional
    public AccountWithdrawResponse withdrawAccount(AccountWithdrawRequest accountWithdrawRequest) {
        final int deposit = accountRepository.findDepositByAccountNumber(accountWithdrawRequest.getAccountNumber())
                .orElseThrow(() -> new AccountIdNotFoundException("계좌를 찾지 못했습니다."));
        final int newValue = deposit - accountWithdrawRequest.getValue();

        if (isDepositAmountValid(newValue)) {
            updateDepositValue(accountWithdrawRequest.getAccountNumber(), newValue);
        } else {
            throw new InsufficientBalanceException("계좌 잔액이 부족합니다.", accountWithdrawRequest.getAccountNumber(), deposit);
        }

        return new AccountWithdrawResponse(accountWithdrawRequest.getAccountNumber(), newValue);
    }

    private boolean isDepositAmountValid(int value) {
        return value >= 0;
    }

    private void updateDepositValue(String accountNumber, int newValue) {
        accountRepository.updateDepositByAccountNumber(accountNumber, newValue)
                .orElseThrow(() -> new CalculateAccountError("계좌를 업데이트하는데 오류가 발생했습니다."));
    }

    private User createFakeUser(String name, String phoneNumber) {
        User newUser = new User(name, phoneNumber);
        return userRepository.save(newUser);
    }

    private List<Account> createFakeAccounts(User user) {
        int numberOfAccounts = random.nextInt(7) + 4;
        List<Account> accounts = new ArrayList<>();

        String fakeAccountNumber = generateRandomAccountNumber();
        int fakeDeposit = generateRandomDeposit();
        String fakeCompanyCode = generateRandomCompanyCode();
        String fakeCategory = "01";

        Account firstAccount = new Account(fakeAccountNumber, fakeDeposit, user, fakeCompanyCode, fakeCategory);
        accounts.add(firstAccount);

        fakeAccountNumber = generateRandomAccountNumber();
        fakeDeposit = generateRandomDeposit();
        fakeCompanyCode = generateRandomCompanyCode();
        fakeCategory = "02";

        Account secondAccount = new Account(fakeAccountNumber, fakeDeposit, user, fakeCompanyCode, fakeCategory);
        accounts.add(secondAccount);

        for (int i = 2; i < numberOfAccounts; i++) {
            fakeAccountNumber = generateRandomAccountNumber();
            fakeDeposit = generateRandomDeposit();
            fakeCompanyCode = generateRandomCompanyCode();
            fakeCategory = generateRandomCategory();

            Account fakeAccount = new Account(fakeAccountNumber, fakeDeposit, user, fakeCompanyCode, fakeCategory);
            accounts.add(fakeAccount);
        }

        accountRepository.saveAll(accounts);
        return accounts;
    }

    private String generateRandomAccountNumber() {
        String firstPart = String.format("%03d", random.nextInt(1000));
        String secondPart = String.format("%04d", random.nextInt(10000));
        String thirdPart = String.format("%05d", random.nextInt(100000));

        return firstPart + "-" + secondPart + "-" + thirdPart;
    }

    private String generateRandomCompanyCode() {
        int code = random.nextInt(5) + 1;
        return String.format("%02d", code);
    }

    private int generateRandomDeposit() {
        int min = 100000;
        int max = 5000000;
        return random.nextInt(max - min + 1) + min;
    }

    private String generateRandomCategory() {
        return random.nextBoolean() ? "01" : "02";
    }

    private void assignStocksToAccountsBasedOnCategory(List<Account> accounts) {
        for (Account account : accounts) {
            switch (account.getCategory()) {
                case "02":
                    List<Stocks> stocksList = createFakeStocks(account);
                    account.setStocksList(stocksList);
                    break;
                case "01":
                    account.setStocksList(new ArrayList<>());
                    break;
                default:
                    account.setStocksList(new ArrayList<>());
                    break;
            }
        }
    }

    private List<Stocks> createFakeStocks(Account account) {
        List<Stocks> stocksList = new ArrayList<>();
        List<String> stockCodes = List.of("005930", "000660", "373220", "207940", "005380", "068270", "000270", "005490", "028260", "035420");

        int count = random.nextInt(10) + 1;

        List<String> availableStockCodes = new ArrayList<>(stockCodes);

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(availableStockCodes.size());
            String stockCode = availableStockCodes.remove(randomIndex);
            int quantity = random.nextInt(191) + 10;

            Stocks fakeStock = new Stocks(quantity, account, stockCode);
            stocksList.add(fakeStock);
        }

        stocksRepository.saveAll(stocksList);
        return stocksList;
    }
}