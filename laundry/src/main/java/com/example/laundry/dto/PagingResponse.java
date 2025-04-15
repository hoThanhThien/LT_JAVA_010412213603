package com.example.laundry.dto;

import java.util.List;

public class PagingResponse<T> {
    private Meta meta;
    private List<T> data;

    public PagingResponse() {
    }

    public PagingResponse(Meta meta, List<T> data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
