package com.md.dzbp.data;

/**
 * 图片信息
 */
public class ImageReceiveMessage extends MessageBase {
    /// 图片链接
    private String PicUrl;

    /// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String MediaId;

    public ImageReceiveMessage() {
    }

    public ImageReceiveMessage(long time, String icon, String name, String picUrl) {
        PicUrl = picUrl;
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
