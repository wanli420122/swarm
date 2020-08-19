package com.infowork.common.api;

/**
 * 封装API的错误码
 * Created by wl on 2020/8/19.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
