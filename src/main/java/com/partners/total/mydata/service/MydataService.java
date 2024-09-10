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

        String randomAccountNumber = firstPart + "-" + secondPart + "-" + thirdPart;
        boolean isExistAccount = accountRepository.existsByAccountNumber(randomAccountNumber);

        if (isExistAccount) {
            randomAccountNumber = generateRandomAccountNumber();
        }

        return randomAccountNumber;
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
        List<String> stockCodes = List.of(
                "005930", "000660", "373220", "207940", "005380", "068270", "000270", "005490", "028260", "035420",
                "006400", "051910", "012330", "329180", "003670", "035720", "259960", "066570", "012450", "033780",
                "009540", "015760", "011200", "003550", "017670", "018260", "034020", "042700", "267260", "402340",
                "010130", "009150", "034730", "096770", "030200", "042660", "010140", "047050", "086280", "003490",
                "326030", "000100", "352820", "090430", "010950", "267250", "011070", "064350", "034220", "450080",
                "047810", "051900", "097950", "028050", "251270", "161390", "010120", "011790", "036460", "009830",
                "078930", "079550", "302440", "036570", "022100", "032640", "454910", "010620", "241560", "128940",
                "271560", "001040", "011780", "006260", "003230", "272210", "000720", "005070", "011170", "004020",
                "035250", "001570", "377300", "002380", "052690", "081660", "004990", "004370", "112610", "361610",
                "001440", "000880", "000150", "000120", "002790", "030000", "008930", "012750", "018880", "007070",
                "006280", "008770", "026960", "028670", "051600", "282330", "006360", "007310", "009420", "017800",
                "023530", "204320", "111770", "139480", "161890", "047040", "000080", "000240", "004170", "004490",
                "185750", "014680", "042670", "103140", "005850", "003090", "005300", "009240", "010060", "011210",
                "073240", "457190", "192820", "280360", "298020", "298050", "137310", "001120", "003620", "004000",
                "009970", "375500", "192080", "014820", "000210", "001740", "001800", "300720", "145720", "285130",
                "120110", "001430", "271940", "003030", "004800", "005250", "005420", "006650", "032350", "039130",
                "069260", "093370", "105630", "114090", "178920", "001680", "000670", "006110", "016380", "009900",
                "008730", "021240", "180640", "066970", "383220", "069620", "069960", "336260", "002710", "105560",
                "055550", "032830", "086790", "138040", "000810", "316140", "024110", "323410", "005830", "006800",
                "029780", "005940", "016360", "071050", "039490", "001450", "138930", "175330", "088350", "139130"
        );

        int count = random.nextInt(10) + 1;

        List<String> availableStockCodes = new ArrayList<>(stockCodes);

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(availableStockCodes.size());
            String stockCode = availableStockCodes.remove(randomIndex);
            int quantity = random.nextInt(10) + 1;

            Stocks fakeStock = new Stocks(quantity, account, stockCode);
            stocksList.add(fakeStock);
        }

        stocksRepository.saveAll(stocksList);
        return stocksList;
    }
}