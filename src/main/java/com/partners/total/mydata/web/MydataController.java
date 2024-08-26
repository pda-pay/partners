package com.partners.total.mydata.web;

import com.partners.total.mydata.dto.*;
import com.partners.total.mydata.service.MydataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MydataController {
    private final MydataService mydataService;

    @PostMapping(value="/mydata")
    public ResponseEntity<MydataResponse> getMydata(@RequestBody MydataRequest mydataRequest) {
        MydataResponse mydataResponse = mydataService.getMydata(mydataRequest);
        return ResponseEntity.status(HttpStatus.OK).body(mydataResponse);
    }

    @PostMapping(value="/mydata/accounts")
    public ResponseEntity<AccountResponse> getAccounts(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = mydataService.getAccounts(accountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }

    @PostMapping(value="/mydata/accounts/deposits")
    public ResponseEntity<DepositResponse> getDepositByAccountId(@RequestBody DepositRequest depositRequest) {
        DepositResponse depositResponse = mydataService.getDepositByAccountId(depositRequest);
        return ResponseEntity.status(HttpStatus.OK).body(depositResponse);
    }

    @PutMapping(value="/mydata/accounts/deposit")
    public ResponseEntity<AccountDepositResponse> depositToAccount(@RequestBody AccountDepositRequest accountDepositRequest) {
        AccountDepositResponse accountDepositResponse = mydataService.depositAccount(accountDepositRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountDepositResponse);
    }

    @PutMapping(value="/mydata/accounts/withdraw")
    public ResponseEntity<AccountWithdrawResponse> withdrawFromAccount(@RequestBody AccountWithdrawRequest accountWithdrawRequest) {
        AccountWithdrawResponse accountWithdrawResponse = mydataService.withdrawAccount(accountWithdrawRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountWithdrawResponse);
    }
}
