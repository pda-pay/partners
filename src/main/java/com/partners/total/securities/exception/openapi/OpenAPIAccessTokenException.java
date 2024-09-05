package com.partners.total.securities.exception.openapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
public class OpenAPIAccessTokenException extends RuntimeException {

    private String message;
    private HttpStatusCode httpStatusCode;

    public OpenAPIAccessTokenException(String errorMessage, HttpStatusCode httpStatusCode) {
        this.message = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }
}
