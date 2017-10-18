package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/22.
 */
public class LocalNetParamsbean implements Serializable{

    private String ip;
    private String mask;//掩码
    private String gateway;//网关
    private String dns;

    public LocalNetParamsbean() {
    }

    public LocalNetParamsbean(String ip, String mask, String gateway, String dns) {
        this.ip = ip;
        this.mask = mask;
        this.gateway = gateway;
        this.dns = dns;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }
}
