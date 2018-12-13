package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 上课页面--课程
 * Created by Administrator on 2017/9/13.
 */
public class CourseBean implements Serializable {

    /**
     * gradeName : 高一年级
     * periodId : 699aab2a-762f-11e6-9296-74d435939039
     * gradeId : 6ef69c90-1959-44ab-a33a-989589d82841
     * address : A3栋101室
     * accountName : 张博
     * photo : null
     * periodName : 下午第一节课
     * className : 1班
     * subjectId : ad2350d0-8e9c-11e5-8baa-1c6f6576b1d6
     * accountId : 342fa921-572e-42f8-8541-6e53d5e92e4f
     * classId : 766bda76-3b5a-434d-a184-86e995291fe3
     * schoolAreaId : a132db64-ffe1-4263-ba77-599c5104ca72
     * subjectName : 英语
     */

    private String gradeName;
    private String periodId;
    private String gradeId;
    private String address;
    private String accountName;
    private String photo;
    private String image;
    private String periodName;
    private String className;
    private String subjectId;
    private String accountId;
    private String classId;
    private String schoolAreaId;
    private String subjectName;
    private String qrcode;

    private String managerAccountId;

    private String managerAccountName;
    ;

    public String getManagerAccountId() {
        return managerAccountId;
    }

    public void setManagerAccountId(String managerAccountId) {
        this.managerAccountId = managerAccountId;
    }

    public String getManagerAccountName() {
        return managerAccountName;
    }

    public void setManagerAccountName(String managerAccountName) {
        this.managerAccountName = managerAccountName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSchoolAreaId() {
        return schoolAreaId;
    }

    public void setSchoolAreaId(String schoolAreaId) {
        this.schoolAreaId = schoolAreaId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
