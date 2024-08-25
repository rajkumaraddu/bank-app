package com.gpact.bank_app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDto {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private Object message;
    private String path;

    public ExceptionResponseDto(int status, Object message) {
        this.status = status;
        this.message = message;
    }

    public ExceptionResponseDto(int status, String error, Object message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
