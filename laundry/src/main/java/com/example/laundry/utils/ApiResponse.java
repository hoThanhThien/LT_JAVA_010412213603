package com.example.laundry.utils;

public class ApiResponse<T> {
    private T data;
    private String message;

    // Constructor for success response with data
    public ApiResponse(T data) {
        this.data = data;
        this.message = null;
    }

    // Constructor for success response with data and message
    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    // Constructor for error response (no data)
    public ApiResponse(String message) {
        this.data = null;
        this.message = message;
    }

    // Static factory methods for better readability
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
