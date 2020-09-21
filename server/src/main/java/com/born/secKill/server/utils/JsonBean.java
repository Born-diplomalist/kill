package com.born.secKill.server.utils;

public class JsonBean<T> {

    private Integer code;
    private String msg;
    private T data;

    public JsonBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonBean(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public JsonBean(StatusCode statusCode, T data) {
        this(statusCode.getCode(),statusCode.getMsg(),data);
    }

    public JsonBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
