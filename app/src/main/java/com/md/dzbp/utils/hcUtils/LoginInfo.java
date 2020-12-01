package com.md.dzbp.utils.hcUtils;

import java.io.Serializable;

public class LoginInfo  implements Serializable {

    public String m_oIPAddr;
    public String m_oPort;
    public String m_oUser;
    public String m_oPsd;
    public String name;

    public LoginInfo(String ip, String port, String user, String psd) {
        this.m_oIPAddr = ip;
        this.m_oPort = port;
        this.m_oUser = user;
        this.m_oPsd = psd;
        this.name = "normal";
    }

//    private static LoginInfo normalLoginInfo = new LoginInfo(NORMAL_IP, NORMAL_PORT, NORMAL_USER, NORMAL_PSD, "normal");


//    public static LoginInfo getNormalLogInfo() {
//        return normalLoginInfo;
//    }

}
