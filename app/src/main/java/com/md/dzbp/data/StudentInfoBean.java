package com.md.dzbp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */
public class StudentInfoBean implements Serializable{

    /**
     * address : A1栋101教室
     * student : {"accountid":"539fc275-aa27-4ffd-9fc9-52072c4241e1","accountname":"师小华","classname":"1班","gradename":"高一年级","photo":"","studentno":"20170915001"}
     * parents : [{"accountid":"27cb067b-873b-48bf-98b3-1a4f1646eaeb","accountname":"师金华","msgcount":2,"photo":null,"relationship":"爸爸","sex":true}]
     */

    private String address;
    private StudentBean student;
    private List<ParentsBean> parents;
    private List<HonorBean> honor;


    public List<HonorBean> getHonor() {
        return honor;
    }

    public void setHonor(List<HonorBean> honor) {
        this.honor = honor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }

    public List<ParentsBean> getParents() {
        return parents;
    }

    public void setParents(List<ParentsBean> parents) {
        this.parents = parents;
    }

    public static class StudentBean {
        /**
         * accountid : 539fc275-aa27-4ffd-9fc9-52072c4241e1
         * accountname : 师小华
         * classname : 1班
         * gradename : 高一年级
         * photo :
         * studentno : 20170915001
         */

        private String accountid;
        private String accountname;
        private String classname;
        private String gradename;
        private String photo;
        private String studentno;

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

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getGradename() {
            return gradename;
        }

        public void setGradename(String gradename) {
            this.gradename = gradename;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getStudentno() {
            return studentno;
        }

        public void setStudentno(String studentno) {
            this.studentno = studentno;
        }
    }

    public static class ParentsBean {
        /**
         * accountid : 27cb067b-873b-48bf-98b3-1a4f1646eaeb
         * accountname : 师金华
         * msgcount : 2
         * photo : null
         * relationship : 爸爸
         * sex : true
         */

        private String accountid;
        private String accountname;
        private int msgcount;
        private String photo;
        private String relationship;
        private boolean sex;
        private boolean select;

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

        public int getMsgcount() {
            return msgcount;
        }

        public void setMsgcount(int msgcount) {
            this.msgcount = msgcount;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public boolean isSex() {
            return sex;
        }

        public void setSex(boolean sex) {
            this.sex = sex;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }
    }

    public static class HonorBean {


        /**
         * description : null
         * id : f5190594-f387-448d-bdbf-8dca40e501f0
         * imagelist : [{"description":null,"id":"2968060507979776","name":"Penguins","thumburl":"http://test.cnzhxy.com/upload/studentHonor/20170922/2968060113715201_thumb_160.jpg","url":"http://test.cnzhxy.com/upload/studentHonor/20170922/2968060113715201.jpg"}]
         * name : 期末考试第一名
         */

        private String description;
        private String id;
        private String name;
        private List<ImagelistBean> imagelist;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public List<ImagelistBean> getImagelist() {
            return imagelist;
        }

        public void setImagelist(List<ImagelistBean> imagelist) {
            this.imagelist = imagelist;
        }

        public static class ImagelistBean {
            /**
             * description : null
             * id : 2968060507979776
             * name : Penguins
             * thumburl : http://test.cnzhxy.com/upload/studentHonor/20170922/2968060113715201_thumb_160.jpg
             * url : http://test.cnzhxy.com/upload/studentHonor/20170922/2968060113715201.jpg
             */

            private String description;
            private String id;
            private String name;
            private String thumburl;
            private String url;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getThumburl() {
                return thumburl;
            }

            public void setThumburl(String thumburl) {
                this.thumburl = thumburl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
