package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/28.
 */
public class MsgSendStatus implements Serializable {
    private int MsgType ;
    private long MsgTime ;
    private int status;


    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public long getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(long msgTime) {
        MsgTime = msgTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
