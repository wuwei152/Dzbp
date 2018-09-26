package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class ScreenShotEvent implements Serializable{
    private int msgid;
    private String deviceId;
    private String data;
    private boolean success;
    private boolean isSend = false;//是否是发送截屏指令

    public ScreenShotEvent(int msgid, String deviceId, String data, boolean success) {
        this.msgid = msgid;
        this.deviceId = deviceId;
        this.data = data;
        this.success = success;
    }
    public ScreenShotEvent(boolean isSend,int msgid, String deviceId) {
        this.msgid = msgid;
        this.deviceId = deviceId;
        this.isSend = isSend;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
