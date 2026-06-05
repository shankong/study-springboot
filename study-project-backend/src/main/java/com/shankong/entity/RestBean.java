package com.shankong.entity;

import lombok.Data;

@Data
public class RestBean <T> {
    private Integer status;
    private boolean success;
    private T message;

    private RestBean(Integer status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, true, null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, true, data);
    }

    public static <T> RestBean<T> failure(Integer status, T data) {
        return new RestBean<>(status, false, data);
    }
}
