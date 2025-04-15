package com.example.laundry.dto;

public class Meta {
    private int page;
    private int size;
    private long totalElements;

    public Meta() {
    }

    public Meta(int page, int size, long totalElements) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}

