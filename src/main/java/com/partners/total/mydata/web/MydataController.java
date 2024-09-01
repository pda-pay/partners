package com.partners.total.mydata.web;

import com.partners.total.mydata.dto.*;
import com.partners.total.mydata.service.MydataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이데이터 API", description = "마이데이터 컨트롤러")
@RequiredArgsConstructor
@RestController
public class MydataController {
    private final MydataService mydataService;

    @Operation(summary = "마이데이터 조회", description = "사용자에 따른 모든 계좌, 증권을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 및 더미 데이터 생성")
    })
    @PostMapping(value="/mydata")
    public ResponseEntity<MydataResponse> getMydata(@RequestBody MydataRequest mydataRequest) {
        MydataResponse mydataResponse = mydataService.getMydata(mydataRequest);
        return ResponseEntity.status(HttpStatus.OK).body(mydataResponse);
    }

    @Operation(summary = "계좌 조회", description = "사용자에 따른 모든 계좌를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾지 못함")
    })
    @PostMapping(value="/mydata/accounts")
    public ResponseEntity<AccountResponse> getAccounts(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = mydataService.getAccounts(accountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }

    @Operation(summary = "계좌 잔액 및 회사 코드 조회", description = "계좌 번호로 계좌 잔액을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "상환 계좌를 찾지 못함")
    })
    @PostMapping(value="/mydata/accounts/deposits")
    public ResponseEntity<AccountForRepaymentResponse> getDepositByAccountId(@RequestBody DepositRequest depositRequest) {
        AccountForRepaymentResponse account = mydataService.getDepositByAccountNumber(depositRequest);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @Operation(summary = "계좌 잔액 증감 요청", description = "계좌 잔액을 증가 시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "계좌를 찾지 못함")
    })
    @PutMapping(value="/mydata/accounts/deposit")
    public ResponseEntity<AccountDepositResponse> depositToAccount(@RequestBody AccountDepositRequest accountDepositRequest) {
        AccountDepositResponse accountDepositResponse = mydataService.depositAccount(accountDepositRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountDepositResponse);
    }

    @Operation(summary = "계좌 잔액 차감 요청", description = "계좌 잔액을 차감 시킵니다. 잔액이 부족 할 시 ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잔액이 부족한 경우, body에 계좌id 와 잔액을 담아 Error를 리턴해줌")
    })
    @PutMapping(value="/mydata/accounts/withdraw")
    public ResponseEntity<AccountWithdrawResponse> withdrawFromAccount(@RequestBody AccountWithdrawRequest accountWithdrawRequest) {
        AccountWithdrawResponse accountWithdrawResponse = mydataService.withdrawAccount(accountWithdrawRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountWithdrawResponse);
    }
}
