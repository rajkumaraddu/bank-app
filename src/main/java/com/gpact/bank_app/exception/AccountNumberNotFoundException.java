package com.gpact.bank_app.exception;


public class AccountNumberNotFoundException extends RuntimeException{
    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}
