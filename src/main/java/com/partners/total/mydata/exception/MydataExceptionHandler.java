package com.partners.total.mydata.exception;

import com.partners.total.mydata.dto.AccountWithdrawResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@ResponseBody
public class MydataExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserNotFoundException.class)
    public String handleDwpNotMatchException(UserNotFoundException exception) {
        log.info("exception class : {}", exception.getClass());
        return "User Not Found!";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = AccountIdNotFoundException.class)
    public String handleAccountIdNotFoundException(AccountIdNotFoundException exception) {
        log.info("exception class : {}", exception.getClass());
        return "Account Not Found!";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InsufficientBalanceException.class)
    public AccountWithdrawResponse handleDwpNotMatchException(InsufficientBalanceException exception) {
        log.info("exception class : {}", exception.getClass());
        return new AccountWithdrawResponse(exception.getAccountNumber(), exception.getDeposit());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = CalculateAccountError.class)
    public String handleDwpNotMatchException(CalculateAccountError exception) {
        log.info("exception class : {}", exception.getClass());
        return "Account Update Error";
    }
}
