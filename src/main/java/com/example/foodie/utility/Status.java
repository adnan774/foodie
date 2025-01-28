package com.example.foodie.utility;

public enum Status {
    SUCCESS("Operation completed successfully"),
    FAILURE("Operation failed due to an error");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
