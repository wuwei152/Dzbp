package com.md.dzbp.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ww
 * @CreateDate: 2021/2/23 15:29
 * @Description:
 */
public class Attendance implements Serializable {

    /**
     * active : false
     * attendancedata : {"bj":[],"cd":[],"yd":[{"attendancetime":"2021-02-23 06:14:37","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669054008262656.PNG","pinyin":"LIANGSITIAN","sex":false,"status":0,"studentid":"6d2a6b63-0db1-416a-955f-c9150a5fb8d3","studentname":"梁思甜"},{"attendancetime":"2021-02-23 06:15:20","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669525460615168.PNG","pinyin":"LINSIQI","sex":true,"status":0,"studentid":"80f22364-fad7-42ff-963d-edf39fb40571","studentname":"林思奇"},{"attendancetime":"2021-02-23 06:18:16","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349662988662996992.PNG","pinyin":"CAIQUANLING","sex":false,"status":0,"studentid":"191f5e1b-bc91-42f7-8a2b-a51666758945","studentname":"蔡泉灵"},{"attendancetime":"2021-02-23 06:19:48","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668174433353728.PNG","pinyin":"LUOQINGCHUAN","sex":true,"status":0,"studentid":"42518bad-f3fc-4124-9dd8-010f4a893001","studentname":"骆晴川"},{"attendancetime":"2021-02-23 06:21:29","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349666984828076032.PNG","pinyin":"HUANGWENYI","sex":false,"status":0,"studentid":"3f672ce5-6c60-4150-a026-72bba1727d5e","studentname":"黄文怡"},{"attendancetime":"2021-02-23 06:21:36","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668385515896832.PNG","pinyin":"YANGZHIWU","sex":true,"status":0,"studentid":"54dfcc68-ba35-433a-9b19-644fb8ac378c","studentname":"杨志武"},{"attendancetime":"2021-02-23 06:21:39","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690398267670528.PNG","pinyin":"WANGSHIYANG","sex":true,"status":0,"studentid":"c2c51065-067b-4208-9d5b-6c6de55e3e28","studentname":"王仕扬"},{"attendancetime":"2021-02-23 06:22:46","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690632012038144.PNG","pinyin":"WUHAIWEN","sex":true,"status":0,"studentid":"d69357b1-5caa-4cab-bd93-5119d30d008c","studentname":"吴海文"},{"attendancetime":"2021-02-23 06:23:34","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664323529605120.PNG","pinyin":"SUNZHUANG","sex":true,"status":0,"studentid":"37ae83d5-0e86-47f1-9cc1-2c18b0315f30","studentname":"孙壮"},{"attendancetime":"2021-02-23 06:23:39","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663976786493440.PNG","pinyin":"WUSHAOJIE","sex":true,"status":0,"studentid":"2f5f44ea-bf28-4586-9ef6-cad45b067831","studentname":"邬少杰"},{"attendancetime":"2021-02-23 06:23:42","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690081195065344.PNG","pinyin":"JIANGYANHAN","sex":true,"status":0,"studentid":"b27a869d-ea4a-4918-a822-99c8db2e407b","studentname":"蒋彦涵"},{"attendancetime":"2021-02-23 06:24:19","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668320416104448.PNG","pinyin":"DAIKANG","sex":true,"status":0,"studentid":"4eca0aea-9314-462d-85db-c719bb05b149","studentname":"戴康"},{"attendancetime":"2021-02-23 06:24:38","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663705725403136.PNG","pinyin":"ZHUXINCAN","sex":true,"status":0,"studentid":"2b1cebe0-b5a9-428a-8d19-e36429bdcdfd","studentname":"祝新灿"},{"attendancetime":"2021-02-23 06:24:44","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663562796105728.PNG","pinyin":"ZHOUDUNMIN","sex":true,"status":0,"studentid":"1f356ad2-c770-432d-b4a0-d047f7ea58e4","studentname":"周敦敏"},{"attendancetime":"2021-02-23 06:25:57","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349689798167625728.PNG","pinyin":"HUYUNFEI","sex":true,"status":0,"studentid":"a6aa325f-aafa-49ac-ab03-4a2eed03adbb","studentname":"胡运飞"},{"attendancetime":"2021-02-23 06:26:12","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349689873853841408.PNG","pinyin":"LESHUAI","sex":true,"status":0,"studentid":"a8dbd301-1abf-474d-8cc1-2f800a3574dc","studentname":"乐帅"},{"attendancetime":"2021-02-23 06:26:14","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690271536775168.PNG","pinyin":"DUANHAO","sex":true,"status":0,"studentid":"baa728de-08ab-43bd-a966-9a7864356abb","studentname":"段昊"},{"attendancetime":"2021-02-23 06:26:21","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349666911918489600.PNG","pinyin":"ZHANGKE","sex":true,"status":0,"studentid":"3c31f801-5b07-460d-a649-d3113474f687","studentname":"张科"},{"attendancetime":"2021-02-23 06:27:45","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690568501886976.PNG","pinyin":"RAOZHANGHAN","sex":true,"status":0,"studentid":"d40da7a1-3d14-489b-891c-de379e70d56a","studentname":"饶章涵"},{"attendancetime":"2021-02-23 06:27:47","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663481539854336.PNG","pinyin":"YINSHICHENG","sex":true,"status":0,"studentid":"1cea4497-38cf-4857-b3f4-e10b7e04fbb0","studentname":"尹世成"},{"attendancetime":"2021-02-23 06:28:24","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669427221626880.PNG","pinyin":"FUANNA","sex":false,"status":0,"studentid":"7460453d-43b7-4582-90fe-ab85908bd973","studentname":"付安娜"},{"attendancetime":"2021-02-23 06:29:38","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349691245923926016.PNG","pinyin":"ZOUCHANG","sex":false,"status":0,"studentid":"ff8e3780-e4d6-4648-866e-17d837afbd8b","studentname":"邹畅"},{"attendancetime":"2021-02-23 06:29:51","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664249860849664.PNG","pinyin":"CHENJIACHENG","sex":true,"status":0,"studentid":"373ed398-d113-4c84-a452-37468d94bcf3","studentname":"陈佳成"},{"attendancetime":"2021-02-23 06:30:37","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668928489521152.PNG","pinyin":"FURAO","sex":true,"status":0,"studentid":"62157356-8850-45be-b491-bff225fdfa25","studentname":"付饶"},{"attendancetime":"2021-02-23 06:30:47","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690687926304768.PNG","pinyin":"HUJIAQI","sex":true,"status":0,"studentid":"d8de4a55-f011-41a6-b74d-83e408369267","studentname":"胡嘉琦"},{"attendancetime":"2021-02-23 06:30:56","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690942554112000.PNG","pinyin":"LENGXINRAN","sex":false,"status":0,"studentid":"ec4e141c-a40c-402f-bec2-be33b6a46638","studentname":"冷欣然"},{"attendancetime":"2021-02-23 06:31:03","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690209767260160.PNG","pinyin":"HUAYUFEI","sex":true,"status":0,"studentid":"b88ce8d1-081e-44b4-b9ea-55a69472a233","studentname":"华宇飞"},{"attendancetime":"2021-02-23 06:31:27","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349662911445860352.PNG","pinyin":"CHENJING","sex":false,"status":0,"studentid":"17d13118-dc8e-4aa2-864c-62d3f85d219d","studentname":"陈晶"},{"attendancetime":"2021-02-23 06:31:30","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690338503032832.PNG","pinyin":"LESHA","sex":false,"status":0,"studentid":"bcd9d34b-d8a2-4a84-af3d-718c0120b22f","studentname":"乐莎"},{"attendancetime":"2021-02-23 06:34:45","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664038748946432.PNG","pinyin":"HULINTONG","sex":true,"status":0,"studentid":"314f8830-e128-440d-b96f-5e1b0edd28e5","studentname":"胡林佟"},{"attendancetime":"2021-02-23 06:35:24","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349691075324805120.PNG","pinyin":"YANGDANQING","sex":false,"status":0,"studentid":"f316b6b7-2fa4-44ed-8a35-389165dad145","studentname":"杨丹青"},{"attendancetime":"2021-02-23 06:35:26","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669824564822016.PNG","pinyin":"CHENGKANG","sex":true,"status":0,"studentid":"831f05f5-5e0a-45e0-ac7f-7396fe919132","studentname":"程康"},{"attendancetime":"2021-02-23 06:35:30","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668456978448384.PNG","pinyin":"YANGMIAO","sex":false,"status":0,"studentid":"54ed168a-7ffd-46de-a018-9e52d04753e8","studentname":"杨妙"},{"attendancetime":"2021-02-23 06:35:34","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668790555639808.PNG","pinyin":"LITIANLE","sex":true,"status":0,"studentid":"5c70b91e-fa8a-4582-9fda-ac9a4deec10c","studentname":"历天乐"},{"attendancetime":"2021-02-23 06:35:36","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669290952884224.PNG","pinyin":"XIAOXI","sex":true,"status":0,"studentid":"7063758b-071e-4b56-82d2-dec99bad1b19","studentname":"肖熙"},{"attendancetime":"2021-02-23 06:35:40","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349691132845490176.PNG","pinyin":"WEIYIMIAO","sex":false,"status":0,"studentid":"f6c6b94f-b4f9-4ead-b79e-b4564ba768c3","studentname":"魏祎苗"},{"attendancetime":"2021-02-23 06:35:44","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664122614054912.PNG","pinyin":"CHENYIKANG","sex":true,"status":0,"studentid":"36746a71-f475-486a-8407-766d12c33a99","studentname":"陈伊康"},{"attendancetime":"2021-02-23 06:35:46","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349689941990309888.PNG","pinyin":"CHENWENGE","sex":true,"status":0,"studentid":"abf91dec-310d-48de-9226-6ca1f76862f8","studentname":"陈文戈"},{"attendancetime":"2021-02-23 06:35:52","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690507588009984.PNG","pinyin":"SHENYUAN","sex":true,"status":0,"studentid":"d01122b8-0cdb-45e9-ad37-5f7868e70c26","studentname":"沈园"},{"attendancetime":"2021-02-23 06:35:56","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349662825735258112.PNG","pinyin":"ZHANGCHENGKAI","sex":true,"status":0,"studentid":"0917eb96-913d-4f2c-a022-68119894aba0","studentname":"张承凯"},{"attendancetime":"2021-02-23 06:35:59","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669364546142208.PNG","pinyin":"LIUYUHAO","sex":true,"status":0,"studentid":"7415c4d4-4fa8-46c1-a975-fc4a46eb5f93","studentname":"刘玉豪"},{"attendancetime":"2021-02-23 06:36:12","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349689441597259776.PNG","pinyin":"ZHENGLITIAN","sex":true,"status":0,"studentid":"971962bf-e200-4b79-9419-45598a0bd339","studentname":"郑力天"},{"attendancetime":"2021-02-23 06:36:49","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669201920393216.PNG","pinyin":"XIAOSHUHAO","sex":true,"status":0,"studentid":"6e773cc1-e6be-4817-abc0-3eefde1bf6cf","studentname":"肖书豪"},{"attendancetime":"2021-02-23 06:37:02","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349691014020857856.PNG","pinyin":"HUTIANYAO","sex":true,"status":0,"studentid":"ec8ef0ab-1d6e-4395-85a9-77d1f8368762","studentname":"胡添尧"},{"attendancetime":"2021-02-23 06:37:08","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668259959406592.PNG","pinyin":"ZHOUMINGZHANG","sex":true,"status":0,"studentid":"4a9f74c6-b56a-41b4-b4d5-d97d32c07bcf","studentname":"周明长"},{"attendancetime":"2021-02-23 06:37:31","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663635017826304.PNG","pinyin":"ZHENGYABING","sex":false,"status":0,"studentid":"2965920f-b2ae-461a-8063-dbe455839cda","studentname":"郑雅冰"},{"attendancetime":"2021-02-23 06:38:34","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690145506328576.PNG","pinyin":"WANXIAOSHAN","sex":true,"status":0,"studentid":"b6a46dcb-de41-4b0c-8e29-94ff89dca43f","studentname":"万小山"},{"attendancetime":"2021-02-23 06:38:57","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690754817064960.PNG","pinyin":"YEFAN","sex":true,"status":0,"studentid":"e3ede406-14ee-4a95-b862-c190f278aac8","studentname":"叶帆"},{"attendancetime":"2021-02-23 06:39:10","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668867596615680.PNG","pinyin":"YURENKANG","sex":true,"status":0,"studentid":"5cedc070-0c79-40db-a02f-87c626026d6f","studentname":"余仁康"},{"attendancetime":"2021-02-23 06:39:57","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668991915786240.PNG","pinyin":"YANGHAOYUAN","sex":true,"status":0,"studentid":"62adb175-57c9-44dd-bf62-cb4b28f0fa63","studentname":"杨浩源"},{"attendancetime":"2021-02-23 06:44:39","content":null,"photo":"http://file.jiaxiaoyj.com/student/1349669944337367040.PNG","pinyin":"GUOZIXIAO","sex":true,"status":0,"studentid":"83a21fd9-df53-4013-8748-2c8fbc63c48d","studentname":"郭梓霄"}],"wd":[{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664195183902720.PNG","pinyin":"CHENZHIPENG","sex":true,"status":1,"studentid":"36b58ac6-acca-40da-8e63-7f9ececa2362","studentname":"陈志鹏"},{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349664405813460992.PNG","pinyin":"LIUFEIYANG","sex":true,"status":1,"studentid":"384ca213-6e28-4315-a9a1-9df6aa9262ff","studentname":"刘飞扬"},{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349668720846307328.PNG","pinyin":"TIANSHENFEI","sex":true,"status":1,"studentid":"55be1b4b-af49-4e6c-86b8-7f444720a36f","studentname":"田沈飞"},{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349690012500754432.PNG","pinyin":"ZHAOTIANSHUO","sex":true,"status":1,"studentid":"ae440396-caed-47cd-ad32-08b5b63579b2","studentname":"赵天烁"},{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349691192228446208.PNG","pinyin":"HUYIFAN","sex":true,"status":1,"studentid":"fd400c00-3b24-42c2-aab9-78763f561408","studentname":"胡一帆"},{"attendancetime":null,"content":null,"photo":"http://file.jiaxiaoyj.com/student/1349663836398944256.PNG","pinyin":"LIUHAOHAN","sex":true,"status":1,"studentid":"2edd08c6-ce20-40a1-aeb5-fe710e1ad0b4","studentname":"刘浩瀚"}],"sj":[]}
     * attendancesettingid : bda3b4ec-e7fb-4be3-adc4-97bb89420665
     * chidaotime : 10:30:00
     * classid : ff053cd3-259c-11eb-be74-fa163e13ab45
     * createtime : 2021-02-23 07:00:15
     * endtime : 06:55:00
     * groupendtime : 10:30:00
     * groupname : 进校
     * groupstarttime : 06:00:00
     * id : b90f2b94-7561-11eb-8bbf-fa163e13ab45
     * recorderid : 00000000-0000-0000-0000-000000000000
     * sendstatus : 0
     * starttime : 06:00:00
     * statusdate : 2021-02-23 00:00:00
     * weather : 晴(北风3级)
     */

    private boolean active;
    private AttendancedataBean attendancedata;
    private String attendancesettingid;
    private String chidaotime;
    private String classid;
    private String createtime;
    private String endtime;
    private String groupendtime;
    private String groupname;
    private String groupstarttime;
    private String id;
    private String recorderid;
    private int sendstatus;
    private String starttime;
    private String statusdate;
    private String weather;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AttendancedataBean getAttendancedata() {
        return attendancedata;
    }

    public void setAttendancedata(AttendancedataBean attendancedata) {
        this.attendancedata = attendancedata;
    }

    public String getAttendancesettingid() {
        return attendancesettingid;
    }

    public void setAttendancesettingid(String attendancesettingid) {
        this.attendancesettingid = attendancesettingid;
    }

    public String getChidaotime() {
        return chidaotime;
    }

    public void setChidaotime(String chidaotime) {
        this.chidaotime = chidaotime;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getGroupendtime() {
        return groupendtime;
    }

    public void setGroupendtime(String groupendtime) {
        this.groupendtime = groupendtime;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupstarttime() {
        return groupstarttime;
    }

    public void setGroupstarttime(String groupstarttime) {
        this.groupstarttime = groupstarttime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecorderid() {
        return recorderid;
    }

    public void setRecorderid(String recorderid) {
        this.recorderid = recorderid;
    }

    public int getSendstatus() {
        return sendstatus;
    }

    public void setSendstatus(int sendstatus) {
        this.sendstatus = sendstatus;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(String statusdate) {
        this.statusdate = statusdate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public static class AttendancedataBean {
        private List<StuBean> bj;
        private List<StuBean> cd;
        private List<StuBean> yd;
        private List<StuBean> wd;
        private List<StuBean> sj;

        public List<StuBean> getBj() {
            return bj;
        }

        public void setBj(List<StuBean> bj) {
            this.bj = bj;
        }

        public List<StuBean> getCd() {
            return cd;
        }

        public void setCd(List<StuBean> cd) {
            this.cd = cd;
        }

        public List<StuBean> getYd() {
            return yd;
        }

        public void setYd(List<StuBean> yd) {
            this.yd = yd;
        }

        public List<StuBean> getWd() {
            return wd;
        }

        public void setWd(List<StuBean> wd) {
            this.wd = wd;
        }

        public List<StuBean> getSj() {
            return sj;
        }

        public void setSj(List<StuBean> sj) {
            this.sj = sj;
        }

        public static class StuBean {
            /**
             * attendancetime : 2021-02-23 06:14:37
             * content : null
             * photo : http://file.jiaxiaoyj.com/student/1349669054008262656.PNG
             * pinyin : LIANGSITIAN
             * sex : false
             * status : 0
             * studentid : 6d2a6b63-0db1-416a-955f-c9150a5fb8d3
             * studentname : 梁思甜
             */

            private String attendancetime;
            private String content;
            private String photo;
            private String pinyin;
            private boolean sex;
            private int status;
            private String studentid;
            private String studentname;

            public String getAttendancetime() {
                return attendancetime;
            }

            public void setAttendancetime(String attendancetime) {
                this.attendancetime = attendancetime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getPinyin() {
                return pinyin;
            }

            public void setPinyin(String pinyin) {
                this.pinyin = pinyin;
            }

            public boolean isSex() {
                return sex;
            }

            public void setSex(boolean sex) {
                this.sex = sex;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStudentid() {
                return studentid;
            }

            public void setStudentid(String studentid) {
                this.studentid = studentid;
            }

            public String getStudentname() {
                return studentname;
            }

            public void setStudentname(String studentname) {
                this.studentname = studentname;
            }
        }
    }
}
