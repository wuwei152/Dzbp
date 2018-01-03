package com.md.dzbp.data;

/**
 * 教室信息
 * Created by Administrator on 2017/9/16.
 */
public class ClassInfoBean {
    /**
     * gradeName : 高一年级
     * classId : 766bda76-3b5a-434d-a184-86e995291fe3
     * gradeId : 6ef69c90-1959-44ab-a33a-989589d82841
     * address : A3栋101室
     * schoolAreaId : a132db64-ffe1-4263-ba77-599c5104ca72
     * className : 1班
     */

    private String gradeName;
    private String classId;
    private String gradeId;
    private String address;
    private String schoolAreaId;
    private String className;
    private String motto; //班训
    private String managerMessage; //班主任寄语
    private String schoolLogo; //班主任寄语
    private String schoolName; //班主任寄语
    private String aliasName; //班级别名

    public ClassInfoBean() {
    }

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

    public String getSchoolAreaId() {
        return schoolAreaId;
    }

    public void setSchoolAreaId(String schoolAreaId) {
        this.schoolAreaId = schoolAreaId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getManagerMessage() {
        return managerMessage;
    }

    public void setManagerMessage(String managerMessage) {
        this.managerMessage = managerMessage;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
