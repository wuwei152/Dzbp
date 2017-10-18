package com.md.dzbp.data;

/**
 * 语音消息
 */
public class VoiceReceiveMessage extends MessageBase {
    /// 语音消息媒体id，可以调用多媒体文件下载接口拉取数据
    private String MediaId;

    /// 语音格式，如amr，speex等
    private String Format;

    /// 语音识别结果，UTF8编码
    private String Recognition;
    //路径
    private String VoicePath;

    private String VoiceLocalPath;

    public VoiceReceiveMessage() {
    }

    public VoiceReceiveMessage(long time, String icon, String name, String path) {
        VoicePath = path;
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getRecognition() {
        return Recognition;
    }

    public void setRecognition(String recognition) {
        Recognition = recognition;
    }

    public String getVoicePath() {
        return VoicePath;
    }

    public void setVoicePath(String voicePath) {
        VoicePath = voicePath;
    }

    public String getVoiceLocalPath() {
        return VoiceLocalPath;
    }

    public void setVoiceLocalPath(String voiceLocalPath) {
        VoiceLocalPath = voiceLocalPath;
    }
}