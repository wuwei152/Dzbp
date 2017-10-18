package com.md.dzbp.model;


/**
 * Created by Administrator on 2016/10/9.
 */
public interface UIDataListener<T>{
    public void loadDataFinish(int code, T data);

    public void showToast(String message);

    public void showDialog();

    public void dismissDialog();

    public void onError(int errorCode, String errorMessage);

    public void cancelRequest();
}
