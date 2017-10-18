package com.md.dzbp.data;

/**
 * 语音消息
 */
public class VoiceSendMessage extends MessageBase{

    // 语音文件路径
    public String VoicePath;

    // 语音文件本地路径
    public String VoiceLocalPath;

    // 是否发送成功
    public int Status;//0正在发送，1发送成功，2发送失败

    public VoiceSendMessage() {
    }

    public VoiceSendMessage(long time, String icon, String name, String voiceLocalPath, int status) {
        VoiceLocalPath = voiceLocalPath;
        Status = status;

        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
    }
    public VoiceSendMessage(int msgType1,long time,String icon,String name,String voiceLocalPath, int status, String voicePath,String from,String to) {
        VoiceLocalPath = voiceLocalPath;
        Status = status;
        VoicePath = voicePath;
        setMsgType(msgType1);
        setCreateTime(time);
        setHeadIcon(icon);
        setUserName(name);
        setFromUserName(from);
        setToUserName(to);
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}