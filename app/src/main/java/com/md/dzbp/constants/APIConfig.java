package com.md.dzbp.constants;

/**
 * API常量
 * Created by Administrator on 2017/9/18.
 */
public interface APIConfig {

    /**
     * 获取会议内容
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_MEETING = "zhxy/api/meeting/index";
    /**
     * 获取学校列表
     * 请求方式：Get
     */
    public static final String GET_SCHOOL = "zhxy/api/device/getSchool";
    /**
     * 获取区域列表
     * 请求方式：Get
     * 参数：?schoolId=eb562777-f335-4598-9812-27b7f37981ef
     */
    public static final String GET_AREA = "zhxy/api/device/getSchoolArea";
    /**
     * 获取设备ID号
     * 请求方式：Get
     * 参数：?schoolId=eb562777-f335-4598-9812-27b7f37981ef&schoolAreaId=a132db64-ffe1-4263-ba77-599c5104ca72&sn=
     */
    public static final String GET_DIVICE_ID = "zhxy/api/device/bind";
    /**
     * 获取当前上课信息
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_COURSE = "zhxy/api/course/teaching";
    /**
     * 获取巡查信息
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_PATROL = "zhxy/api/inspection/index";
    /**
     * 获取巡查信息
     * 请求方式：Post
     * 参数：
     * {
     * deviceId
     * classId: 'xxxx',
     * teacherId: 'xxxxx',
     * periodId: 'xxxxx',
     * subjectId: 'xxxxxx',
     * createAccountId: 'xxxx',
     * scores: [
     * {
     * parameterId: 'xxxxxxx',
     * positive: true | false
     * }
     * ]
     * <p>
     * }
     */
    public static final String Post_PATROL = "zhxy/api/inspection/save";
    /**
     * 获取紧急公告
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_NOTICE = "zhxy/api/notice/index";
    /**
     * 获取主页信息
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_Main = "zhxy/api/course/schedule";
    /**
     * 获取主页信息
     * 请求方式：Get
     * 参数：?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     * &studentId=2952492693
     */
    public static final String GET_STUDENT = "zhxy/api/student/index";
    /**
     * 获取历史消息
     * 请求方式：Get
     * 参数：
     * ?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     * &studentId=539fc275-aa27-4ffd-9fc9-52072c4241e1
     * &parentId=27cb067b-873b-48bf-98b3-1a4f1646eaeb
     */
    public static final String GET_STUDENT_HISTORY_MSG = "zhxy/api/student/msg";
    /**
     * 标记消息为已读
     * 请求方式：Get
     * 参数：
     * ?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     * &studentId=539fc275-aa27-4ffd-9fc9-52072c4241e1
     * &parentId=27cb067b-873b-48bf-98b3-1a4f1646eaeb
     */
    public static final String GET_READ_MSG = "zhxy/api/student/readedmsg";
    /**
     * 主页获取未读消息
     * 请求方式：Get
     * 参数：
     * ?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_LOAD_MSG = "zhxy/api/course/loadMsg";
    /**
     * 主页获取公告列表
     * 请求方式：Get
     * 参数：
     * ?deviceId=e9d50961-3a47-460f-bfc9-1398e3b64c4e
     */
    public static final String GET_LOAD_NOTICE = "zhxy/api/notice/list";
    /**
     * 获取班级签到信息
     * 请求方式：Get
     * 参数：
     * ?deviceId=6dd62d53-249e-4845-8716-05926842568f
     */
    public static final String GET_SIGN = "zhxy/api/attendance/index";
    /**
     * 获取考试信息
     * 请求方式：Get
     * 参数：
     * ?deviceId=6dd62d53-249e-4845-8716-05926842568f
     */
    public static final String GET_EXAM = "zhxy/api/exam/info";

    /**
     * 获取一卡通余额
     * 请求方式：Get
     * 参数：
     * ?studentId=e6d0a4b6-57f0-45c4-b2c1-2fb8b01d4e88
     */
    public static final String GET_BALANCE = "zhxy/api/student/getBalance";
    /**
     * 获取图书借阅
     * 请求方式：Get
     * 参数：
     */
    public static final String GET_DZLENDLIST= "zhxy/api/book/dzLendList";


    /**
     * 获取考勤
     * 请求方式：Get
     * 参数：
     */
    public static final String getAllClassAttendanceDetail= "zhxy/api/attendance/getAllClassAttendanceDetail";

}
