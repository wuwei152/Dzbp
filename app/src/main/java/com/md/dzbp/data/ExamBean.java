package com.md.dzbp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/14.
 */

public class ExamBean implements Serializable{

    /**
     * address :
     * examination : {"name":"","startdate":"","enddate":"","examinationid":"","remark":""}
     * plan : [{"id":"","edate":"","starttime":"","endtime":"","subjectname":"","teachername":""}]
     */

    private String address;
    private ExaminationBean examination;
    private List<ExamPlan> plan;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ExaminationBean getExamination() {
        return examination;
    }

    public void setExamination(ExaminationBean examination) {
        this.examination = examination;
    }

    public List<ExamPlan> getPlan() {
        return plan;
    }

    public void setPlan(List<ExamPlan> plan) {
        this.plan = plan;
    }

    public static class ExaminationBean  implements Serializable{

        public ExaminationBean() {
        }

        public ExaminationBean(String name, String startdate, String enddate) {
            this.name = name;
            this.startdate = startdate;
            this.enddate = enddate;
        }

        /**
         * name :
         * startdate :
         * enddate :
         * examinationid :
         * remark :
         */



        private String name;
        private String startdate;
        private String enddate;
        private String examinationid;
        private String remark;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getExaminationid() {
            return examinationid;
        }

        public void setExaminationid(String examinationid) {
            this.examinationid = examinationid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
