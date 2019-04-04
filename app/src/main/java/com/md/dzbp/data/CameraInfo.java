package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 摄像头信息
 * Created by Administrator on 2017/12/4.
 */

public class CameraInfo implements Serializable {
    private static final long serialVersionUID = -7142261422095352209L;

    private String ip;
    private String port;
    private String username;
    private String psw;
    private int isPlay;

    public CameraInfo() {
    }

    public CameraInfo(String ip, String port, String username, String psw) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.psw = psw;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
