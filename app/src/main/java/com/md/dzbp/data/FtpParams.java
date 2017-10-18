package com.md.dzbp.data;

import java.io.Serializable;

/**
 * ftp参数
 * Created by Administrator on 2017/8/29.
 */
public class FtpParams implements Serializable{
    private String IP;
    private  int Port;
    private String UserName;
    private String Psw;
    private String WebApi;


    public FtpParams() {
    }

    public FtpParams(String IP, int port, String userName, String psw, String webApi) {
        this.IP = IP;
        Port = port;
        UserName = userName;
        Psw = psw;
        WebApi = webApi;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPsw() {
        return Psw;
    }

    public void setPsw(String psw) {
        Psw = psw;
    }

    public String getWebApi() {
        return WebApi;
    }

    public void setWebApi(String webApi) {
        WebApi = webApi;
    }
}
