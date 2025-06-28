package com.projectsky.loyaltysystem.exception;

public class PurchaseAlreadyRefundedException extends RuntimeException {
    public PurchaseAlreadyRefundedException(String message) {
        super(message);
    }
}
