package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 文本消息
 */
public class TextSendMessage extends MessageBase implements Serializable{
    private String Content;

    public TextSendMessage() {
    }

    public TextSendMessage(long time, String icon, String name, String content) {
        Content = content;
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
    }

    public TextSendMessage(int msgType1,long time,String icon,String name, String content,String from,String to) {
        Content = content;
        setMsgType(msgType1);
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
        setFromUserName(from);
        setToUserName(to);
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}