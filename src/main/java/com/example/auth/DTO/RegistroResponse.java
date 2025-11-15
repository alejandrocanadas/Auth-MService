package com.example.auth.DTO;

public class RegistroResponse {
    private String message;

    public RegistroResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}