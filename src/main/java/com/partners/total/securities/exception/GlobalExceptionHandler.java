package com.partners.total.securities.exception;

import com.partners.total.securities.exception.account.AccountNotFoundException;
import com.partners.total.securities.exception.openapi.OpenAPICurrentPriceException;
import com.partners.total.securities.exception.openapi.OpenAPIPreviousClosePriceException;
import com.partners.total.securities.exception.stocks.StockSellException;
import com.partners.total.securities.exception.stocks.StocksNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> catchStocksNotFoundException(StocksNotFoundException e) {
        log.error("exception class: {}", e.getClass());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> catchStockSellException(StockSellException e) {
        log.error("exception class: {}", e.getClass());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> catchOpenAPIPreviousCloserPriceException(OpenAPIPreviousClosePriceException e) {
        log.error("exception class: {}", e.getClass());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> catchOpenAPICurrentPriceException(OpenAPICurrentPriceException e) {
        log.error("exception class: {}", e.getClass());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> catchAccountNotFoundException(AccountNotFoundException e) {
        log.error("exception class: {}", e.getClass());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @Getter
    @Setter
    @Builder
    public static class ErrorDTO {

        private String message;

        public ErrorDTO() {}

        public ErrorDTO(String message) {
            this.message = message;
        }
    }
}
