package com.partners.total.securities.service;

import com.partners.total.mydata.domain.*;
import com.partners.total.securities.dto.*;
import com.partners.total.securities.exception.account.AccountNotFoundException;
import com.partners.total.securities.exception.stocks.StockSellException;
import com.partners.total.securities.exception.stocks.StocksNotFoundException;
import com.partners.total.securities.utils.StockData;
import com.partners.total.securities.utils.StockOAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecuritiesService {

    private final StockOAuth stockOAuthService;
    private final StockData stockData;
    private final AccountRepository accountRepository;
    private final StocksRepository stocksRepository;

    private String accessToken = null;

    @Transactional
    public List<AllStocksRequestDTO> getAllStocksByAccountNumber(String accountNumber) {

        Account account = accountRepository
                .findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("조회되는 계좌 정보가 없습니다."));

        List<AllStocksRequestDTO> allStocksRequestsDTO = new ArrayList<>();

        for (Stocks stock : account.getStocksList()) {

            AllStocksRequestDTO allStocksRequestDTO = AllStocksRequestDTO
                    .builder()
                    .id(stock.getId())
                    .quantity(stock.getQuantity())
                    .stockCode(stock.getStockCode())
                    .build();

            allStocksRequestsDTO.add(allStocksRequestDTO);
        }

        return allStocksRequestsDTO;
    }

    @Transactional
    public Map<String, ?> requestSellStocks(StockPriorityDTO stockPriorityDTO) {

        String accountNumber = stockPriorityDTO.getAccountNumber();
        String stockCode = stockPriorityDTO.getStockCode();

        Account account = accountRepository
                .findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("조회되는 계좌 정보가 없습니다."));

        Stocks stocks = stocksRepository
                .findStocksByAccountIdAndStockCode(
                        account.getId(),
                        stockCode
                )
                .orElseThrow(() -> new StocksNotFoundException("보유중인 증권이 아닙니다. 증권 코드: " + stockPriorityDTO.getStockCode()));

        if (stocks.getQuantity() - stockPriorityDTO.getQuantity() < 0) {
            throw new StockSellException("보유중인 증권 수가 매도량보다 적습니다. 보유량: " + stocks.getQuantity() + " 매도 요청량: " + stockPriorityDTO.getQuantity());
        }

        int currentPriceOfStock = Integer.parseInt(getCurrentPrice(stockPriorityDTO.getStockCode()).getOutput().getStck_prpr());

        int sellAmount = stockPriorityDTO.getQuantity() * currentPriceOfStock;

        stocks.minusQuantity(stockPriorityDTO.getQuantity());

        stocksRepository.save(stocks);

        Map<String, Integer> map = new HashMap<>();
        map.put("sellAmount", sellAmount);
        return map;
    }

    public PreviousPricesDTO getPreviousClosePriceList(StockCodesDTO stockCodesDTO) {

        checkoutToken();

        PreviousPricesDTO previousPricesDTO = new PreviousPricesDTO();

        for (String stockCode : stockCodesDTO.getStockCodes()) {

            ClosePriceDTO closePriceDTO = stockData.fetchClosePriceData(stockCode, accessToken);

            previousPricesDTO.addPreviousDTO(stockCode, closePriceDTO.getOutput1().getStck_prdy_clpr());
        }

        return previousPricesDTO;
    }

    @Scheduled(cron = "0 0 0 * * *") // 자정
    protected void getOAuthToken() {
        accessToken = stockOAuthService.getAccessToken(accessToken);
    }

    private void checkoutToken() {
        if (accessToken == null) {
            getOAuthToken();
        }
    }

    private CurrentPriceDTO getCurrentPrice(String code) {

        checkoutToken();

        return stockData.fetchCurrentPriceData(code, accessToken);
    }
}
