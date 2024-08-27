package com.partners.total.securities.web;

import com.partners.total.mydata.domain.Stocks;
import com.partners.total.securities.dto.AllStocksRequestDTO;
import com.partners.total.securities.dto.PreviousPricesDTO;
import com.partners.total.securities.dto.StockCodesDTO;
import com.partners.total.securities.dto.StockPriorityDTO;
import com.partners.total.securities.service.SecuritiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "증권사 API", description = "증권사 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/securities")
public class SecuritiesController {

    private final SecuritiesService securitiesService;

    @Operation(summary = "단일 계좌 모든 증권 조회", description = "계좌에 따른 모든 증권을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회 실패")
    })
    @GetMapping("/accounts/stocks/{accountNumber}")
    public ResponseEntity<?> getAllAccounts(
            @Parameter(description = "조회할 계좌의 번호", required = true)
            @PathVariable String accountNumber
    ) {

        List<AllStocksRequestDTO> stocks = securitiesService.getAllStocksByAccountNumber(accountNumber);

        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @Operation(summary = "증권 매도 요청", description = "반대 매매 시 요청 받는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매도 요청 성공"),
            @ApiResponse(responseCode = "400", description = "매도 요청 실패")
    })
    @PutMapping("/accounts/stocks")
    public ResponseEntity<?> requestSellStocks(@RequestBody StockPriorityDTO stockPriorityDTO) {

        Map<String, ?> amount = securitiesService.requestSellStocks(stockPriorityDTO);

        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    @Operation(summary = "증권 전일 종가 요청", description = "주식 코드 리스트를 받아서 그에 대한 전일 종가를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "증권 전일 종가 성공"),
            @ApiResponse(responseCode = "400", description = "증권 전일 종가 실패")
    })
    @GetMapping("/stocks")
    public ResponseEntity<?> getPreviousClosePrice(@RequestBody StockCodesDTO stockCodesDTO) {

        PreviousPricesDTO previousPricesDTO = securitiesService.getPreviousClosePriceList(stockCodesDTO);

        return new ResponseEntity<>(previousPricesDTO, HttpStatus.OK);
    }
}
