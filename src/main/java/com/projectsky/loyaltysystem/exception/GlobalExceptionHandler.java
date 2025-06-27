package com.projectsky.loyaltysystem.exception;

import com.projectsky.loyaltysystem.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Client not found", e.getMessage());
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClientAlreadyExistsException(ClientAlreadyExistsException e) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Client already exists", e.getMessage());
    }

    @ExceptionHandler(PurchaseAlreadyRefundedException.class)
    public ResponseEntity<ErrorResponse> handlePurchaseAlreadyRefundedException(PurchaseAlreadyRefundedException e) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Purchase already refunded", e.getMessage());
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePurchaseNotFoundException(PurchaseNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Purchase not found", e.getMessage());
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughBalanceException(NotEnoughBalanceException e) {
        return buildResponseEntity(HttpStatus.PAYMENT_REQUIRED, "Not enough balance", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String error, String message){
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), error, message));
    }
}
