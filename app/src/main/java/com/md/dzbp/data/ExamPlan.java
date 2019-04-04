package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 考试科目
 */
public class ExamPlan implements Serializable{
    private  String id;
    private  String edate;
    private String 	starttime;
    private String endtime;
    private String subjectname;
    private String teachername;

    public ExamPlan() {
    }

    public ExamPlan(String id, String edate, String starttime, String endtime, String subjectname, String teachername) {
        this.id = id;
        this.edate = edate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.subjectname = subjectname;
        this.teachername = teachername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }
}
