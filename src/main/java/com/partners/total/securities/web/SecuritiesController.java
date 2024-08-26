package com.partners.total.securities.web;

import com.partners.total.mydata.domain.Stocks;
import com.partners.total.securities.dto.CurrentPriceDTO;
import com.partners.total.securities.dto.StockCodesDTO;
import com.partners.total.securities.dto.StockPriorityDTO;
import com.partners.total.securities.dto.StocksDTO;
import com.partners.total.securities.service.SecuritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/securities")
public class SecuritiesController {

    private final SecuritiesService securitiesService;

    @GetMapping("/accounts/stock/{accountId}")
    public ResponseEntity<?> getAllAccounts(@PathVariable int accountId) {

        List<Stocks> stocks = securitiesService.getAllStocksByAccountId(accountId);

        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    // 증권 매도 요청
    @PutMapping("/stocks")
    public ResponseEntity<?> requestSellStocks(@RequestBody StockPriorityDTO stockPriorityDTO) {

        Map<String, ?> amount = securitiesService.requestSellStocks(stockPriorityDTO);

        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    // 증권 전일 종가 요청
    @GetMapping("/stocks")
    public ResponseEntity<?> getPreviousClosePrice(@RequestBody StockCodesDTO stockCodesDTO) {

        Map<String, String> previousClosePriceList = securitiesService.getPreviousClosePriceList(stockCodesDTO);

        return new ResponseEntity<>(previousClosePriceList, HttpStatus.OK);
    }
}
