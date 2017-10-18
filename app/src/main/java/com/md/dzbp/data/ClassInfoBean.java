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
}
