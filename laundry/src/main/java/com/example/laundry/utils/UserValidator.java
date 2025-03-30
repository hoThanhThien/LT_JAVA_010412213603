package com.example.laundry.utils;

import java.util.regex.Pattern;

public class UserValidator {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH && Pattern.matches(PASSWORD_PATTERN, password);
    }

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    public static boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}

