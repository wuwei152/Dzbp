package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/15.
 */
public class HistoryMsg implements Serializable{

    /**
     * content : 下雨啦
     * createtime : 2017-09-15 10:27:20
     * hasview : false
     * id : 1147083423744
     * msgtype : 1
     * receiveid : 539fc275-aa27-4ffd-9fc9-52072c4241e1
     * res : null
     * sendid : 27cb067b-873b-48bf-98b3-1a4f1646eaeb
     */

    private String content;
    private String createtime;
    private boolean hasview;
    private String id;
    private int msgtype;
    private String receiveid;
    private String res;
    private String sendid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public boolean isHasview() {
        return hasview;
    }

    public void setHasview(boolean hasview) {
        this.hasview = hasview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }
}
