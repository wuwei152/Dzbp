package com.md.dzbp.tcp;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface FileHandle {
    public void handleSuccess(int code, String data);
    public void handleFail(int code, String data);
    public void handleFinished(int code, String data);
}
