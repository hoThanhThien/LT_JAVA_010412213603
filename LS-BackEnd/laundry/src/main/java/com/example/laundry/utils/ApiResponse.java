package com.example.laundry.utils;

public class ApiResponse<T> {
    private final String message;
    private final T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message) {
        this.message = message;
        data = null;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
