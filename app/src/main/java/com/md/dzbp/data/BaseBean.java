package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/12.
 */
public class BaseBean<T> implements Serializable{

    private T data;
    private int status;//0失败  1成功
    private String msg;//失败信息

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
