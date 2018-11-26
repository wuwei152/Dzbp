package com.md.dzbp.data;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.model.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/10/12.
 */

public class WorkTimePoint implements Serializable {

    private String TaskTag;//任务标志
    private String Name;//任务名
    private int Type;//任务类型               0　作息时间 1　会议 2 通知 3考试  4 考勤  100开关屏
    private String TriggerTime;//触发时间
    private int Taskstate;//是否已执行 0否  1 是
    private int StartTask;//是开始任务0   还是结束任务1

    public WorkTimePoint() {
    }

    public WorkTimePoint(String taskTag, String name, int type, String triggerTime, int taskstate, int taskType) {
        TaskTag = taskTag;
        Name = name;
        Type = type;
        TriggerTime = triggerTime;
        Taskstate = taskstate;
        StartTask = taskType;
    }

    public String getTaskTag() {
        return TaskTag;
    }

    public void setTaskTag(String taskTag) {
        TaskTag = taskTag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTriggerTime() {
        return TriggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        TriggerTime = triggerTime;
    }

    public int getTaskstate() {
        return Taskstate;
    }

    public void setTaskstate(int taskstate) {
        Taskstate = taskstate;
    }

    public int getStartTask() {
        return StartTask;
    }

    public void setStartTask(int startTask) {
        StartTask = startTask;
    }

    /**
     * 将任务时间段拆解为任务时间点
     *
     * @param list
     * @return
     */
    public static ArrayList<WorkTimePoint> GetWorkTimePointList(List<WorkTimePeriod> list) {
        ArrayList<WorkTimePoint> mList = new ArrayList<>();
        if (list != null) {
            for (WorkTimePeriod period : list) {
                //将打卡考勤设置为作息时间
                if (period.getTriggerTime_A().contains("?:?:?")) {
                    period.setTriggerTime_A(period.getTriggerTime_A().replace("?:?:?", TimeUtils.getCurrentDate()));
                }
                mList.add(new WorkTimePoint(period.getTaskTag(), period.getName(), period.getType(), period.getTriggerTime_A(), 0, 0));
                if (!TextUtils.isEmpty(period.getTriggerTime_B())) {
                    if (period.getTriggerTime_B().contains("?:?:?")) {
                        period.setTriggerTime_B(period.getTriggerTime_B().replace("?:?:?", TimeUtils.getCurrentDate()));
                    }
                    mList.add(new WorkTimePoint(period.getTaskTag(), period.getName(), period.getType(), period.getTriggerTime_B(), 0, 1));
                }
            }
        }
        ListSort(mList);
        return mList;
    }

    /**
     * 对任务按时间排序
     */
    private static void ListSort(List<WorkTimePoint> list) {
        Collections.sort(list, new Comparator<WorkTimePoint>() {
            @Override
            public int compare(WorkTimePoint o1, WorkTimePoint o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getTriggerTime());
                    Date dt2 = format.parse(o2.getTriggerTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
