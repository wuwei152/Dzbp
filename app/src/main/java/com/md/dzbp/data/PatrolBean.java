package com.md.dzbp.data;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */
public class PatrolBean {


    /**
     * classInfo : {"gradeName":"高一年级","classId":"0e7f1ca1-1d44-48ef-818e-81d3c0a19cf5","gradeId":"98aa463d-2289-47e4-abb3-e43174959895","address":"A1栋101教室","schoolAreaId":"0bf41fbc-b61c-4e16-bfe2-0b3bdfeca1db","className":"1班"}
     * teacher : {"accountId":"02eaae7a-926e-49f2-a593-6330cf11caa3","periodId":"698f32c2-762f-11e6-9296-74d435939039","accountName":"赵鹏","photo":null,"periodName":"上午第二节课","subjectId":"6e9fa26d-8e1b-11e5-8baa-1c6f6576b1d6","subjectName":"语文"}
     * inspectionParameters : [{"id":"c4bab186-730f-11e6-9296-74d435939039","parametername":"教师是否按时到岗"},{"id":"c4babab3-730f-11e6-9296-74d435939039","parametername":"教师授课行为是否符合规范"},{"id":"c4babc6b-730f-11e6-9296-74d435939039","parametername":"教师授课用语是否符合规范"},{"id":"c4babdc6-730f-11e6-9296-74d435939039","parametername":"学生出勤是否正常"},{"id":"c4babf19-730f-11e6-9296-74d435939039","parametername":"课堂纪律是否正常"}]
     * classManager : {"accountId":"02eaae7a-926e-49f2-a593-6330cf11caa3","accountName":"赵鹏","subjects":["语文"],"photo":null}
     */
    private AttendanceBean attendance;
    private ClassInfoBean classInfo;
    private TeacherBean teacher;
    private ClassManagerBean classManager;
    private List<InspectionParametersBean> inspectionParameters;

    public ClassInfoBean getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfoBean classInfo) {
        this.classInfo = classInfo;
    }

    public TeacherBean getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherBean teacher) {
        this.teacher = teacher;
    }

    public ClassManagerBean getClassManager() {
        return classManager;
    }

    public void setClassManager(ClassManagerBean classManager) {
        this.classManager = classManager;
    }

    public List<InspectionParametersBean> getInspectionParameters() {
        return inspectionParameters;
    }

    public void setInspectionParameters(List<InspectionParametersBean> inspectionParameters) {
        this.inspectionParameters = inspectionParameters;
    }

    public AttendanceBean getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceBean attendance) {
        this.attendance = attendance;
    }

    public static class AttendanceBean {

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

    public static class TeacherBean {
        /**
         * accountId : 02eaae7a-926e-49f2-a593-6330cf11caa3
         * periodId : 698f32c2-762f-11e6-9296-74d435939039
         * accountName : 赵鹏
         * photo : null
         * periodName : 上午第二节课
         * subjectId : 6e9fa26d-8e1b-11e5-8baa-1c6f6576b1d6
         * subjectName : 语文
         */

        private String accountId;
        private String periodId;
        private String accountName;
        private Object photo;
        private String periodName;
        private String subjectId;
        private String subjectName;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getPeriodId() {
            return periodId;
        }

        public void setPeriodId(String periodId) {
            this.periodId = periodId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
            this.photo = photo;
        }

        public String getPeriodName() {
            return periodName;
        }

        public void setPeriodName(String periodName) {
            this.periodName = periodName;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }

    public static class InspectionParametersBean {
        /**
         * id : c4bab186-730f-11e6-9296-74d435939039
         * parametername : 教师是否按时到岗
         */

        private String id;
        private String parametername;
        private boolean positive = true;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParametername() {
            return parametername;
        }

        public void setParametername(String parametername) {
            this.parametername = parametername;
        }

        public boolean isPositive() {
            return positive;
        }

        public void setPositive(boolean positive) {
            this.positive = positive;
        }
    }
}
