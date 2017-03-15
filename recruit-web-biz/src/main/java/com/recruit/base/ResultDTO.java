package com.recruit.base;

import java.io.Serializable;

/**
 * Created by zhuangjt on 2017-03-15.
 */
public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = -1575945136395379123L;

    private boolean success;

    private String errorCode;

    private String errorMsg;

    private T model;

    public static ResultDTO failed(String errorCode, String errorMasg) {
        return new ResultDTO(Boolean.FALSE, errorCode, errorMasg, null);
    }

    public static ResultDTO failed(String errorMsg) {
        return new ResultDTO(Boolean.FALSE, null, errorMsg, null);
    }

    public static <T> ResultDTO<T> succeed(T model) {
        return new ResultDTO(Boolean.TRUE, null, null, model);
    }

    private ResultDTO(boolean success, String errorCode, String errorMsg, T model) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isUnSuccess() {
        return !(this.isSuccess());
    }
}
