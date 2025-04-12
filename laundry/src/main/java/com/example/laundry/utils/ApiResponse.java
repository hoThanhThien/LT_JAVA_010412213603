<<<<<<< HEAD
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
=======
package com.example.laundry.utils;

public class ApiResponse<T> {
    private final String message;
    private final T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
