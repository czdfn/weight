package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/7/30.
 */
public class BaseBean<T> implements Serializable{

    private int infoCode;
    private String message;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return infoCode == 200;
    }
}
