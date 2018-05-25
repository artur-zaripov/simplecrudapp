package io.github.artur_zaripov.simplecrudapp.model;

import java.time.LocalDateTime;

public class ApiResponse {

    private LocalDateTime timestamp;

    private String message;

    private String details;

    public ApiResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = "";
    }

    public ApiResponse(String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
