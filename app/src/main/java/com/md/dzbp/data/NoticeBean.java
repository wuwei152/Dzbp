package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class NoticeBean implements Serializable {

    /**
     * publishTime : 2017-09-13 09:50:06
     * publisherName : 张博
     * endShowTime : 2017-09-14 18:38:10
     * title : 紧急公告
     * schoolName : 智慧校园工作组
     * content : <p>丰富的速度</p>
     * urgent 是否为紧急公告
     */

    private String publishTime;
    private String publisherName;
    private String endShowTime;
    private String title;
    private String schoolName;
    private String content;
    private boolean urgent;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getEndShowTime() {
        return endShowTime;
    }

    public void setEndShowTime(String endShowTime) {
        this.endShowTime = endShowTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
