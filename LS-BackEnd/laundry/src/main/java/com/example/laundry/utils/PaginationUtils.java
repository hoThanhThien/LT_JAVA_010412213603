package com.example.laundry.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {
  public static final int MAX_PAGE_SIZE = 100;

  private PaginationUtils() {
  }

  public static PageRequest of(int page, int size) {
    validate(page, size);
    return PageRequest.of(page, size);
  }

  public static PageRequest of(int page, int size, Sort sort) {
    validate(page, size);
    return sort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, sort);
  }

  private static void validate(int page, int size) {
    if (page < 0) {
      throw new IllegalArgumentException("Chỉ số trang (page) phải >= 0");
    }
    if (size < 1 || size > MAX_PAGE_SIZE) {
      throw new IllegalArgumentException("Kích thước trang (size) phải trong khoảng từ 1 đến " + MAX_PAGE_SIZE);
    }
  }
}

