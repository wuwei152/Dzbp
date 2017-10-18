package com.md.dzbp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public class Meetingbean implements Serializable{


    /**
     * address : A3栋101室
     * endTime : 2017-09-12 12:30:00
     * id : 9980cb09-e445-4e7c-b6ce-7bd1a0c03da4
     * meetingUserList : [{"accountId":"342fa921-572e-42f8-8541-6e53d5e92e4f","accountName":"张博","host":true},{"accountId":"7d5bece5-6df4-4d2c-9eff-be4c2cb8b2e9","accountName":"付奇","host":false}]
     * name : 这是测试会议
     * qrcodeUrl : https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQFI8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyWWhvOE1weU9lUzMxeVVUVDFwY18AAgSwMLdZAwQIBwAA
     * startTime : 2017-09-12 12:00:00
     * summary : 东方闪电
     */

    private String address;
    private String endTime;
    private String id;
    private String name;
    private String qrcodeUrl;
    private String startTime;
    private String summary;
    private List<MeetingUserListBean> meetingUserList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<MeetingUserListBean> getMeetingUserList() {
        return meetingUserList;
    }

    public void setMeetingUserList(List<MeetingUserListBean> meetingUserList) {
        this.meetingUserList = meetingUserList;
    }

    public static class MeetingUserListBean implements Serializable{
        /**
         * accountId : 342fa921-572e-42f8-8541-6e53d5e92e4f
         * accountName : 张博
         * host : true
         */

        private String accountId;
        private String accountName;
        private int signinStatus; //1已签到 0未签到
        private boolean host;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public int getSigninStatus() {
            return signinStatus;
        }

        public void setSigninStatus(int signinStatus) {
            this.signinStatus = signinStatus;
        }

        public boolean getHost() {
            return host;
        }

        public void setHost(boolean host) {
            this.host = host;
        }
    }
}
