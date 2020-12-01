package com.md.dzbp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public class MainData implements Serializable {

    /**
     * chat : [{"accountid":"4a61925c-48f2-416a-872d-d49f1e4aed6b","accountname":"吴伟111","count":1,"photo":null},{"accountid":"e34f2bb1-3322-4dd9-a1fe-30d120dc7614","accountname":"张博555","count":2,"photo":null}]
     * course : [{"accountname":"万亮","period":"8:10-8:50","serial":1,"subjectname":"生物"},{"accountname":"罗泷","period":"9:00-9:40","serial":2,"subjectname":"化学"},{"accountname":"吴伟","period":"10:00-10:40","serial":3,"subjectname":"物理"},{"accountname":"王永刚","period":"10:50-11:30","serial":4,"subjectname":"语文"},{"accountname":"张博","period":"13:30-14:10","serial":5,"subjectname":"数学"},{"accountname":"张博","period":"14:30-15:10","serial":6,"subjectname":"英语"},{"accountname":"万亮","period":"15:40-16:20","serial":7,"subjectname":"政治"},{"accountname":"吴伟","period":"16:30-17:10","serial":8,"subjectname":"物理"},{"accountname":"罗泷","period":"17:40-18:30","serial":9,"subjectname":"化学"}]
     * class : {"gradeName":"高一年级","classId":"766bda76-3b5a-434d-a184-86e995291fe3","gradeId":"6ef69c90-1959-44ab-a33a-989589d82841","address":"A3栋101室","schoolAreaId":"a132db64-ffe1-4263-ba77-599c5104ca72","className":"1班"}
     * classManager : {"accountId":"342fa921-572e-42f8-8541-6e53d5e92e4f","accountName":"张博","subjects":["英语","数学"],"photo":null}
     * notice : {"content":"<p>丰富的速度<\/p>","createaccountid":"342fa921-572e-42f8-8541-6e53d5e92e4f","publishtime":"2017-09-13 09:50:06","title":"紧急公告"}
     */

    private ClassInfoBean classInfo;
    private AttendanceBean attendance;
    private ClassManagerBean classManager;
    private List<NoticeBean> notice;
    private List<ChatBean> chat;
    private List<CourseBean> course;
    private ArrayList<PhotosBean> photos;
    private List<MoralScoreBean> moralScore;

    public ClassInfoBean getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfoBean classInfo) {
        this.classInfo = classInfo;
    }

    public ClassManagerBean getClassManager() {
        return classManager;
    }

    public void setClassManager(ClassManagerBean classManager) {
        this.classManager = classManager;
    }

    public List<NoticeBean> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeBean> notice) {
        this.notice = notice;
    }

    public List<ChatBean> getChat() {
        return chat;
    }

    public void setChat(List<ChatBean> chat) {
        this.chat = chat;
    }

    public List<CourseBean> getCourse() {
        return course;
    }

    public void setCourse(List<CourseBean> course) {
        this.course = course;
    }

    public ArrayList<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotosBean> photos) {
        this.photos = photos;
    }

    public List<MoralScoreBean> getMoralScore() {
        return moralScore;
    }

    public void setMoralScore(List<MoralScoreBean> moralScore) {
        this.moralScore = moralScore;
    }

    public AttendanceBean getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceBean attendance) {
        this.attendance = attendance;
    }

    public static class NoticeBean implements Serializable {
        /**
         * content : <p>丰富的速度</p>
         * createaccountid : 342fa921-572e-42f8-8541-6e53d5e92e4f
         * publishtime : 2017-09-13 09:50:06
         * title : 紧急公告
         */

        private String noticeId;
        private String publishtime;
        private String title;
        private String publisher;
        private String publisherPhoto;

        public NoticeBean() {
        }

        public NoticeBean(String title, String publishtime) {
            this.publishtime = publishtime;
            this.title = title;
        }

        public String getPublisherPhoto() {
            return publisherPhoto;
        }

        public void setPublisherPhoto(String publisherPhoto) {
            this.publisherPhoto = publisherPhoto;
        }

        public String getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(String noticeId) {
            this.noticeId = noticeId;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ChatBean implements Serializable {
        /**
         * accountid : 4a61925c-48f2-416a-872d-d49f1e4aed6b
         * accountname : 吴伟111
         * count : 1
         * photo : null
         */

        private String accountid;
        private String accountname;
        private int count;
        private String photo;

        public String getAccountid() {
            return accountid;
        }

        public void setAccountid(String accountid) {
            this.accountid = accountid;
        }

        public String getAccountname() {
            return accountname;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    public static class CourseBean implements Serializable {
        /**
         * accountname : 万亮
         * period : 8:10-8:50
         * serial : 1
         * subjectname : 生物
         * remarks
         */

        private String accountname;
        private String period;
        private int serial;
        private String subjectname;
        private String remarks;
        private String startTime;
        private String endTime;
        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getAccountname() {
            return accountname;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public String getSubjectname() {
            return subjectname;
        }

        public void setSubjectname(String subjectname) {
            this.subjectname = subjectname;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public static class AttendanceBean implements Serializable {

        /**
         * yindao : 3
         * shidao : 3
         * weidao : 0
         */

        private int yindao;
        private int shidao;
        private int weidao;

        public int getYindao() {
            return yindao;
        }

        public void setYindao(int yindao) {
            this.yindao = yindao;
        }

        public int getShidao() {
            return shidao;
        }

        public void setShidao(int shidao) {
            this.shidao = shidao;
        }

        public int getWeidao() {
            return weidao;
        }

        public void setWeidao(int weidao) {
            this.weidao = weidao;
        }
    }

    public static class PhotosBean implements Serializable {

        /**
         * description : 的范德萨发梵蒂冈地方个梵蒂冈梵蒂冈梵蒂冈梵蒂冈放大嘎达删除相册V型从V型从
         * name : Hydrangeas
         * url : /upload/album/20170919/1896564276592641.jpg
         */

        private String description;
        private String name;
        private String url;
        private String thumburl;
        private String mimeType;
        private boolean checked;

        public String getThumburl() {
            return thumburl;
        }

        public void setThumburl(String thumburl) {
            this.thumburl = thumburl;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class MoralScoreBean implements Serializable {

        /**
         * id : ded95542-4da5-4611-b3cc-88dce55aa4d6
         * name : 清洁卫生
         * score : 59
         */

        private String id;
        private String name;
        private int score;

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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

}
