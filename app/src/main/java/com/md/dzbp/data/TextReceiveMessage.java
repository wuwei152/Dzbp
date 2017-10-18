package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 文本消息
 */
public class TextReceiveMessage extends MessageBase implements Serializable{
    private String Content;

    public TextReceiveMessage() {
    }

    public TextReceiveMessage(long time, String icon, String name, String content) {
        Content = content;
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}