package com.partners.total.securities.service;

import com.partners.total.mydata.domain.*;
import com.partners.total.securities.dto.*;
import com.partners.total.securities.utils.StockData;
import com.partners.total.securities.utils.StockOAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SecuritiesService {

    private final StockOAuth stockOAuthService;
    private final StockData stockData;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StocksRepository stocksRepository;

    private String accessToken = null;

//    public ClosePriceDTO getPreviousClosePrice(String code) {
//
//        checkoutToken();
//
//        return stockData.fetchClosePriceData(code, accessToken);
//    }

    private CurrentPriceDTO getCurrentPrice(String code) {

        checkoutToken();

        return stockData.fetchCurrentPriceData(code, accessToken);
    }

    @Transactional
    public Map<String, ?> requestSellStocks(StockPriorityDTO stockPriorityDTO) {

        Stocks stocks = stocksRepository
                .findStocksByAccountIdAndStockCode(
                        stockPriorityDTO.getAccountId(),
                        stockPriorityDTO.getStockCode()
                )
                .orElseThrow(NoSuchElementException::new);

        if (stocks.getQuantity() - stockPriorityDTO.getQuantity() < 0) {

            return null;
        }

        System.out.println("stocks.getQuantity() = " + stocks.getQuantity());

        int currentPriceOfStock = Integer.parseInt(getCurrentPrice(stockPriorityDTO.getStockCode()).getOutput().getStck_prpr());

        int sellAmount = stockPriorityDTO.getQuantity() * currentPriceOfStock;

        int restQuantity = stocks.getQuantity() - stockPriorityDTO.getQuantity();

        System.out.println("restQuantity = " + restQuantity);

        stocksRepository
                .updateQuantityByAccountIdAndQuantity(stocks.getId(), restQuantity)
                .orElseThrow(() -> new NoSuchElementException("그런 스톡 없어용"));

        Map<String, Integer> map = new HashMap<>();
        map.put("sellAmount", sellAmount);
        return map;
    }

    public Map<String, String> getPreviousClosePriceList(StockCodesDTO stockCodesDTO) {

        checkoutToken();

        Map<String, String> previousClosePriceList = new HashMap<>();

        for (String stockCode : stockCodesDTO.getStockCodes()) {

            ClosePriceDTO closePriceDTO = stockData.fetchClosePriceData(stockCode, accessToken);

            previousClosePriceList.put(stockCode, closePriceDTO.getOutput1().getStck_prdy_clpr());
        }

        return previousClosePriceList;
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

    public List<Stocks> getAllStocksByAccountId(int accountId) {

        return stocksRepository.findStocksByAccountId(accountId);
    }
}
