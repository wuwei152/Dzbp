package com.md.dzbp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class SignInfoBean implements Serializable {

    /**
     * classInfo : {"gradeName":"高一年级","classId":"0e7f1ca1-1d44-48ef-818e-81d3c0a19cf5","gradeId":"98aa463d-2289-47e4-abb3-e43174959895","address":"A1栋101教室","className":"1班"}
     * student : [{"accountid":"357f8055-2828-4e52-8e85-56ab4a3c8ba3","accountname":"吴大伟","state":0},{"accountid":"539fc275-aa27-4ffd-9fc9-52072c4241e1","accountname":"师小华","state":0},{"accountid":"89f1f402-0cb9-49b1-9d07-8dbb5c193b49","accountname":"赵小鹏","state":0}]
     */

    private ClassInfoBean classInfo;
    private List<StudentBean> student;

    public ClassInfoBean getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfoBean classInfo) {
        this.classInfo = classInfo;
    }

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public static class ClassInfoBean {
        /**
         * gradeName : 高一年级
         * classId : 0e7f1ca1-1d44-48ef-818e-81d3c0a19cf5
         * gradeId : 98aa463d-2289-47e4-abb3-e43174959895
         * address : A1栋101教室
         * className : 1班
         */

        private String gradeName;
        private String classId;
        private String gradeId;
        private String address;
        private String className;

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    public static class StudentBean {
        /**
         * accountid : 357f8055-2828-4e52-8e85-56ab4a3c8ba3
         * accountname : 吴大伟
         * state : 0
         */

        private String accountid;
        private String accountname;
        private String photo;
        private int state;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
